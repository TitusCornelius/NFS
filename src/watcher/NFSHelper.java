package watcher;
import java.nio.file.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import org.acplt.oncrpc.OncRpcClientAuth;
import org.acplt.oncrpc.OncRpcClientAuthUnix;
import org.acplt.oncrpc.OncRpcClientStub;
import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.OncRpcProtocols;

import client.mount.*;
import client.nfs.*;
import nfsv1.NFSClient;


/*
 *  1.d restore  on demand.
 *  read files from server and write them locally
 */

public class NFSHelper {
    private final String host;
    private final String mountDir;
    private final int uid;
    private final int gid;
    private final String username;
	private final NFSClient nfsc;
	public NFSHelper(String host, String mountDir, int uid, int gid, String username) throws Exception {
		this.host     = host;
		this.mountDir = mountDir;
		this.uid      = uid;
		this.gid      = gid;
		this.username = username;
		
		nfsc = new NFSClient(host, mountDir, uid, gid, username, null);
	}

	public void restore(String remotePath, String localPath) throws IOException, OncRpcException {
	    if (!localPath.startsWith("/")) {
	        System.out.println("Please provide the path as an absolute path (starting with '/'), rooted at the NFS mount!");
	        return;
	    }
	    System.out.format("Restore %s at %s\n", remotePath, localPath);
	    fattr attributes = nfsc.getAttr(remotePath);
	    if (attributes == null) {
	        System.err.println("No such path!");
	        return;
	    }
	    List<String> parts = Arrays.asList(remotePath.split("/"));
	    Path dir = Paths.get(localPath).resolve(String.join("/", parts.subList(1, parts.size() -1)));
	    Path entity = Paths.get(localPath).resolve(String.join("/", parts.subList(1, parts.size())));
	    System.out.format("Creating '%s' directory\n", dir);
	    Files.createDirectories(dir);
	    switch (attributes.type) {
	        case ftype.NFREG:
	            String contents = nfsc.readFile(remotePath);
	            Files.write(entity, contents.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	            break;
	        case ftype.NFDIR:
	            Files.createDirectories(entity);
	            break;
	        default:
	            System.err.format("Unsupported file type: %d\n", attributes.type);
	    }
	}


	public static void main(String[] args) throws Exception {
        String host       = args.length > 1 ? args[0] : "localhost";
        String mountDir   = args.length > 1 ? args[1] : "/exports";
        String remotePath = args.length > 1 ? args[2] : "/a/b/c/d/e/f/";
        String localPath  = args.length > 1 ? args[3] : "/Users/cornelius/Dropbox/USI courses/Eclipse work space/DS_project/NFS/test";
        int uid           = NFSClient.getUID();
        int gid           = NFSClient.getGID();
        String username   = System.getProperty("user.name");
        
        NFSHelper nfsh   = new NFSHelper(host, mountDir, uid, gid, username);
        nfsh.restore(remotePath, localPath);
    }
}
