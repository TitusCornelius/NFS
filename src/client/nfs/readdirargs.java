/*
 * Automatically generated by jrpcgen 1.1.1 on 12/1/15 11:39 PM
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package client.nfs;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class readdirargs implements XdrAble {
    public fhandle dir;
    public nfscookie cookie;
    public int count;

    public readdirargs() {
    }

    public readdirargs(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        dir.xdrEncode(xdr);
        cookie.xdrEncode(xdr);
        xdr.xdrEncodeInt(count);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        dir = new fhandle(xdr);
        cookie = new nfscookie(xdr);
        count = xdr.xdrDecodeInt();
    }

}
// End of readdirargs.java
