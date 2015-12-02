package nfsv1;

import java.io.*;
import java.net.InetAddress;
import java.util.*;

import org.acplt.oncrpc.OncRpcClientAuth;
import org.acplt.oncrpc.OncRpcClientAuthUnix;
import org.acplt.oncrpc.OncRpcClientStub;
import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.OncRpcProtocols;

import client.mount.*;
import client.nfs.*;
public class NFSClient {
    private final fhandle root;
    private final nfsClient nfs;
    public NFSClient(String host, String mntPoint) throws IOException, OncRpcException {
        mountClient mnt = new mountClient(InetAddress.getByName(host), OncRpcProtocols.ONCRPC_UDP);
        OncRpcClientAuth auth = new OncRpcClientAuthUnix("zaikunxu", 1000, 50);
        mnt.getClient().setAuth(auth);
        fhstatus fh = mnt.MOUNTPROC_MNT_1(new dirpath(mntPoint));
        if (fh.status == 0) {
            byte[] fhandle = fh.directory.value;
            root = new fhandle(fhandle);
            nfs = new nfsClient(InetAddress.getByName(host), OncRpcProtocols.ONCRPC_UDP);
            nfs.getClient().setAuth(auth);
        } else {
            root = null;
            nfs = null;
        }
    }

    public fhandle getRoot() {
        return root;
    }

    public nfsClient getNfs() {
        return nfs;
    }

    public fhandle lookup(fhandle dir, filename name) {

        return null;

    }

    public fattr getAttr(fhandle file) {

        return null;
    }

    public synchronized List<entry> readDir(fhandle folder) throws IOException, OncRpcException {
        List<entry> entries = new ArrayList<entry>();

        readdirargs in = new readdirargs();
        in.dir = folder;
        in.cookie = new nfscookie(new byte[] {0,0,0,0});
        in.count = 8000;

        readdirres out = nfs.NFSPROC_READDIR_2(in);

        if (out.status == stat.NFS_OK) {
            entry e = out.readdirok.entries;

            while ( e != null) {
                entries.add(e);
                e = e.nextentry;
            }
        }

        return entries;
    }
    public static void main(String[] args) throws IOException, OncRpcException {
    	NFSClient client = new NFSClient("localhost", "/exports");
        assert(client.nfs != null);
        assert(client.root != null);
//        fhandle dir = client.getRoot();
//        List<entry> ls = client.readDir(dir);
//        
//        ls.stream().forEach(e -> {
//	        	fhandle file = client.lookup(dir, e.name); // get fhandle to get attributes below
//	        	fattr attr = client.getAttr(file); // to get attributes	
//	//            if (attr != null) {
//	//                 // use attributes to differentiate, e.g. if directory or not
//	//            }
//	        	System.out.println(e.name.value);
//
//
//        	}
//
//        );



    }


}