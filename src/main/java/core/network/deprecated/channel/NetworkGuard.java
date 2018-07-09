/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
 * by the @authors tag. 
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

package core.network.deprecated.channel;

import java.net.InetSocketAddress;


@Deprecated
public interface NetworkGuard {

    /**
     * Decides whether a remote peer is secure or not.
     * 
     * @param channel The channel who received the packet.
     * @param source The address of the remote peer.
     * @return Returns true if source is considered secure; otherwise, returns false.
     */
    boolean isSecure(NetworkChannel channel, InetSocketAddress source);

}
