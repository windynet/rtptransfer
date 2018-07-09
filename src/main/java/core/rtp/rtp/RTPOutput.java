/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package core.rtp.rtp;

import core.component.AbstractSink;
import core.component.audio.AudioOutput;
import core.rtp.RTPDataChannel;
import core.scheduler.PriorityQueueScheduler;
import core.spi.FormatNotSupportedException;
import core.spi.format.AudioFormat;
import core.spi.format.FormatFactory;
import core.spi.format.Formats;
import core.spi.memory.Frame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

//import core.spi.dsp.Processor;

/**
 * Transmitter implementation.
 * 
 * @author Yulian oifa
 */
public class RTPOutput extends AbstractSink {

	private static final long serialVersionUID = 3227885808614338323L;

	private static final Logger logger = LogManager.getLogger(RTPOutput.class);

	private AudioFormat format = FormatFactory.createAudioFormat("LINEAR", 8000, 16, 1);

	@Deprecated
	private RTPDataChannel channel;

	private RtpTransmitter transmitter;

	// active formats
	private Formats formats;

	// signaling processor
//	private Processor dsp;

	private AudioOutput output;

	/**
	 * Creates new transmitter
	 */
	@Deprecated
	public RTPOutput(PriorityQueueScheduler scheduler, RTPDataChannel channel) {
		super("Output");
		this.channel = channel;
		output = new AudioOutput(scheduler, 1);
		output.join(this);
	}

	protected RTPOutput(PriorityQueueScheduler scheduler, RtpTransmitter transmitter) {
		super("Output");
		this.transmitter = transmitter;
		output = new AudioOutput(scheduler, 1);
		output.join(this);
	}

	public AudioOutput getAudioOutput() {
		return this.output;
	}

	@Override
	public void activate() {
		output.start();
	}

	@Override
	public void deactivate() {
		output.stop();
	}

	/**
	 * Assigns the digital signaling processor of this component. The DSP allows
	 * to get more output formats.
	 * 
	 * @param dsp
	 *            the dsp instance
	 */

	/**
	 * Gets the digital signaling processor associated with this media source
	 * 
	 * @return DSP instance.
	 */

	public void setFormats(Formats formats) throws FormatNotSupportedException {
		this.formats = formats;
	}

	@Override
	public void onMediaTransfer(Frame frame) throws IOException {
		// do transcoding

		if (this.transmitter != null) {
			this.transmitter.send(frame);
		}

		// XXX deprecated code
		if (this.channel != null) {
			channel.send(frame);
		}

	}
}
