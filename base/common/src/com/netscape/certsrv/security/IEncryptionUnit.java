// --- BEGIN COPYRIGHT BLOCK ---
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; version 2 of the License.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along
// with this program; if not, write to the Free Software Foundation, Inc.,
// 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
//
// (C) 2007 Red Hat, Inc.
// All rights reserved.
// --- END COPYRIGHT BLOCK ---
package com.netscape.certsrv.security;

import java.security.PublicKey;

import org.mozilla.jss.crypto.CryptoToken;
import org.mozilla.jss.crypto.PrivateKey;
import org.mozilla.jss.crypto.SymmetricKey;
import org.mozilla.jss.crypto.SymmetricKey.Type;

import com.netscape.certsrv.base.EBaseException;

/**
 * An interface represents a encryption unit.
 *
 * @version $Revision$, $Date$
 */
public interface IEncryptionUnit extends IToken {

    /**
     * Retrieves the public key in this unit.
     *
     * @return public key
     */
    public PublicKey getPublicKey();

    /**
     * Wraps data. The given key will be wrapped by the
     * private key in this unit.
     *
     * @param priKey private key to be wrapped
     * @return wrapped data
     * @exception EBaseException failed to wrap
     */
    public byte[] wrap(PrivateKey priKey) throws Exception;

    /**
     * Wraps data. The given key will be wrapped by the
     * private key in this unit.
     *
     * @param symKey symmetric key to be wrapped
     * @return wrapped data
     * @exception EBaseException failed to wrap
     */
    public byte[] wrap(SymmetricKey symKey) throws Exception;

    /**
     * Verifies the given key pair.
     *
     * @param publicKey public key
     * @param privateKey private key
     */
    public void verify(PublicKey publicKey, PrivateKey privateKey) throws
            EBaseException;

    /**
     * Unwraps data. This method rebuilds the private key by
     * unwrapping the private key data.
     *
     * @param sessionKey session key that unwrap the private key
     * @param symmAlgOID symmetric algorithm
     * @param symmAlgParams symmetric algorithm parameters
     * @param privateKey private key data
     * @param pubKey public key
     * @return private key object
     * @throws Exception
     */
    public PrivateKey unwrap(byte sessionKey[], String symmAlgOID,
            byte symmAlgParams[], byte privateKey[],
            PublicKey pubKey)
            throws Exception;

    /**
     * Unwraps data. This method rebuilds the private key by
     * unwrapping the private key data.
     *
     * @param symmAlgOID symmetric algorithm
     * @param symmAlgParams symmetric algorithm parameters
     * @param pubKey public key
     * @param transportCert transport certificate
     * @return private key object
     * @throws Exception
     */
    public PrivateKey unwrap(byte encSymmKey[], String symmAlgOID,
            byte symmAlgParams[], byte encValue[], PublicKey pubKey,
            org.mozilla.jss.crypto.X509Certificate transportCert)
            throws Exception;

    /**
     * Unwraps symmetric key data. This method rebuilds the symmetric key by
     * unwrapping the private data blob.
     *
     * @param wrappedKeyData symmetric key data wrapped up with session key
     * @return Symmetric key object
     * @exception Exception failed to unwrap
     */

    public SymmetricKey unwrap(byte wrappedKeyData[], SymmetricKey.Type algorithm, int keySize)
            throws Exception;

    /**
     * Unwraps symmetric key . This method
     * unwraps the symmetric key.
     *
     * @param sessionKey session key that unwrap the symmetric key
     * @param symmAlgOID symmetric algorithm
     * @param symmAlgParams symmetric algorithm parameters
     * @param symmetricKey  symmetric key data
     * @param type symmetric key algorithm
     * @param strength symmetric key strength in bytes
     * @return Symmetric key object
     * @throws Exception
     */

    public SymmetricKey unwrap_symmetric(byte sessionKey[], String symmAlgOID,
            byte symmAlgParams[], byte symmetricKey[], Type type, int strength)
            throws Exception;

    /**
     * Unwraps symmetric key . This method
     * unwraps the symmetric key.
     *
     * @param encSymmKey wrapped symmetric key to be unwrapped
     * @return Symmetric key object
     */

    public SymmetricKey unwrap_session_key(CryptoToken token, byte encSymmKey[],
            SymmetricKey.Usage usage, WrappingParams params);

    public PrivateKey unwrap_temp(byte privateKey[], PublicKey pubKey)
            throws Exception;

    /**
     * Unwraps data. This method rebuilds the private key by
     * unwrapping the private key data.
     *
     * @param privateKey private key data
     * @param pubKey public key object
     * @return private key object
     * @throws Exception
     */
    public PrivateKey unwrap(byte privateKey[], PublicKey pubKey)
            throws Exception;

    /**
     * Encrypts the internal private key (private key to the KRA's
     * internal storage).
     *
     * @param rawPrivate user's private key (key to be archived)
     * @return encrypted data
     * @exception EBaseException failed to encrypt
     */
    public byte[] encryptInternalPrivate(byte rawPrivate[]) throws Exception;

    /**
     * Decrypts the internal private key (private key from the KRA's
     * internal storage).
     *
     * @param wrappedPrivateData unwrapped private key data (key to be recovered)
     * @return raw private key
     * @throws Exception
     */
    public byte[] decryptInternalPrivate(byte wrappedPrivateData[])
            throws Exception;

    /**
     * Decrypts the external private key (private key from the end-user).
     *
     * @param sessionKey session key that protects the user private
     * @param symmAlgOID symmetric algorithm
     * @param symmAlgParams symmetric algorithm parameters
     * @param privateKey private key data
     * @return private key data
     * @throws Exception
     */
    public byte[] decryptExternalPrivate(byte sessionKey[],
            String symmAlgOID,
            byte symmAlgParams[], byte privateKey[])
            throws Exception;

    /**
     * Decrypts the external private key (private key from the end-user).
     *
     * @param sessionKey session key that protects the user private
     * @param symmAlgOID symmetric algorithm
     * @param symmAlgParams symmetric algorithm parameters
     * @param privateKey private key data
     * @param transportCert transport certificate
     * @return private key data
     * @throws Exception
     */
    public byte[] decryptExternalPrivate(byte sessionKey[],
            String symmAlgOID, byte symmAlgParams[], byte privateKey[],
            org.mozilla.jss.crypto.X509Certificate transportCert)
            throws Exception;
}
