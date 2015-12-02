/*
 * Automatically generated by jrpcgen 1.1.1 on 12/1/15 11:39 PM
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package client.nfs;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class statfsres implements XdrAble {
    public int status;
    public statfsresOK info;

    public statfsres() {
    }

    public statfsres(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        xdr.xdrEncodeInt(status);
        switch ( status ) {
        case stat.NFS_OK:
            info.xdrEncode(xdr);
            break;
        default:
            break;
        }
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        status = xdr.xdrDecodeInt();
        switch ( status ) {
        case stat.NFS_OK:
            info = new statfsresOK(xdr);
            break;
        default:
            break;
        }
    }

}
// End of statfsres.java
