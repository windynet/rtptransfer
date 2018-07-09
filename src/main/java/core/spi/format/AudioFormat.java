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

package core.spi.format;

/**
 * Descriptor for audio format.
 *
 * @author kulikov
 */
public class AudioFormat extends Format implements Cloneable {
    //sampling frequency
    private int sampleRate;
    //bits per sample
    private int sampleSize = -1;
    //number of channels
    private int channels = 1;

    /**
     * Creates new audio format descriptor.
     *
     * @param name the encoding name.
     */
    protected AudioFormat(EncodingName name) {
        super(name);
    }

    /**
     * Creates new format descriptor
     *
     * @param name the encoding
     * @param sampleRate sample rate value in Hertz
     * @param sampleSize sample size in bits
     * @param channels number of channels
     */
    private AudioFormat(EncodingName name, int sampleRate, int sampleSize, int channels) {
        super(name);
        this.sampleRate = sampleRate;
        this.sampleSize = sampleSize;
        this.channels = channels;
    }

    /**
     * Gets the sampling rate.
     *
     * @return the sampling rate value in hertz.
     */
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * Modifies sampling rate value.
     *
     * @param sampleRate the sampling rate in hertz.
     */
    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    /**
     * Gets the sample size.
     *
     * @return sample size in bits.
     */
    public int getSampleSize() {
        return sampleSize;
    }

    /**
     * Modifies sample size.
     *
     * @param sampleSize sample size in bits.
     */
    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    /**
     * Gets the number of channels.
     *
     * The default value is 1.
     *
     * @return the number of channels
     */
    public int getChannels() {
        return channels;
    }

    /**
     * Modifies number of channels.
     *
     * @param channels the number of channels.
     */
    public void setChannels(int channels) {
        this.channels = channels;
    }

    @Override
    public AudioFormat clone() {
        AudioFormat f = new AudioFormat(getName().clone(), sampleRate, sampleSize, channels);
        f.setOptions(this.getOptions());
        return f;
    }

    @Override
    public boolean matches(Format other) {
        if (!super.matches(other)) return false;

        AudioFormat f = (AudioFormat) other;

        if (f.sampleRate != this.sampleRate) return false;
        // XXX dirty patch for issue #7 - https://github.com/Mobicents/mediaserver/issues/7
//        if (f.sampleSize != this.sampleSize) return false;
        if (f.channels != this.channels) return false;

        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AudioFormat[");
        builder.append(getName().toString());

        builder.append(",");
        builder.append(sampleRate);

        if (sampleSize > 0) {
            builder.append(",");
            builder.append(sampleSize);
        }

        if (channels == 1) {
            builder.append(",mono");
        } else if (channels == 2) {
            builder.append(",stereo");
        } else {
            builder.append(",");
            builder.append(channels);
        }

        builder.append("]");
        return builder.toString();
    }
}
