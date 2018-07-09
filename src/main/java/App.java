import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import Process.RtpTransfer;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        logger.debug( "RTP Transfer Start !!!" );

        RtpTransfer rtpTransfer = new RtpTransfer();
        rtpTransfer.start();
    }
}
