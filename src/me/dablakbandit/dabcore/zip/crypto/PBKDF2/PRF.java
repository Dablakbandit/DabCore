

package me.dablakbandit.dabcore.zip.crypto.PBKDF2;



interface PRF
{
    public void init(byte[] P);

    public byte[] doFinal(byte[] M);

    public int getHLen();
}
