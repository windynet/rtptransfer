package Process;

import Audio.AMRWB;
import common.StringUtil;
import core.rtp.MockWallClock;
import core.rtp.rtp.RtpClock;
import core.rtp.rtp.RtpPacket;
import core.rtp.rtp.SsrcGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x3.player.core.codec.AMRSlience;
//import x3.player.mru.session.SessionManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RtpTransfer {

    private static final Logger logger = LoggerFactory.getLogger( RtpTransfer.class);


    long base_timestamp = 1000000L;
    int defaultModeset = 8;
    private int seqNumber;
    private long ssrc;

    private MockWallClock wallClock = new MockWallClock();
    static ScheduledFuture<?> scheduleFuture;
    private ScheduledExecutorService scheduleService;

    private Rtptransmit rtptransmit;

//    private Scheduler scheduler;
//
//    private UdpManager udpManager;
//    private RTPDataChannel channelbase_timestamp;
//
//    private ChannelsManager channelsManager;
//    private PriorityQueueScheduler mediaScheduler;

    private InetSocketAddress dst;
    private DatagramSocket socket;

    private RtpClock clock = new RtpClock(wallClock);

    public void start() {
        long timestamp = -1;
        int seqNumber = 0;

        dst = new InetSocketAddress("192.168.5.70", 10240);

        try {
            socket = new DatagramSocket(new InetSocketAddress("192.168.5.70", 9200));
        } catch (SocketException e) {
            e.printStackTrace();
        }

        ssrc = SsrcGenerator.generateSsrc();

        scheduleService = Executors.newScheduledThreadPool( 1);
        rtptransmit = new Rtptransmit();

        scheduleFuture = scheduleService.scheduleAtFixedRate( rtptransmit, 20, 19, TimeUnit.MILLISECONDS);
    }

    private void rtpPacketWrap(int seqNumber,long ssrc)  {
        RtpPacket rtpPacket = new RtpPacket( 512, true);
        AMRWB amrwb = new AMRWB( 172, true);

        AMRSlience amrSlience = new AMRSlience();

        long timestamp = this.generateTimeStamp();

        logger.debug( "ssrc : " + ssrc );

        int dataSize = amrSlience.getPayloadSize(defaultModeset);
        if (dataSize == 0) {
            return;
        }

        byte[] data = new byte[dataSize+1];

        data[0] = (byte)0xf0;
        amrSlience.copySilenceBuffer(defaultModeset, data, 1);


        logger.debug( "AMR Data : "+data.length+" [" + StringUtil.byteArrayToHex( data) + "]");

        logger.debug( "wrap length : " +rtpPacket.getLength());

        rtpPacket.wrap( true, amrwb.AMRWB_PAYLOAD_TYPE,seqNumber,timestamp*seqNumber,ssrc,data,0,data.length);

        logger.debug( "wrap length : " +rtpPacket.getLength());

        try {

            byte[] senddata = new byte[rtpPacket.getLength()];

            ByteBuffer sendBuff = rtpPacket.getBuffer();

            sendBuff.get( senddata,0, rtpPacket.getLength());


            DatagramPacket p = new DatagramPacket( senddata, 0, senddata.length, dst);

            logger.debug("data : length " + senddata.length + " [" + byteArrayToHex(senddata) +"]");

            socket.send( p );
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for(final byte b: a)
            sb.append(String.format("%02x ", b&0xff));
        return sb.toString();
    }

    private long generateTimeStamp()
    {
        long timestamp =  base_timestamp/ 100000L;

        RtpClock rtpClock = new RtpClock(wallClock);
        rtpClock.setClockRate( 32000 );

        // convert to rtp time units
        timestamp = rtpClock.convertToRtpTime(timestamp);

        logger.debug( "Time Stamp : " + timestamp );

        return timestamp;
    }

    class Rtptransmit implements Runnable {


        @Override

        public void run() {
            rtpPacketWrap( seqNumber,ssrc );

            seqNumber ++;
        }
    }

}
