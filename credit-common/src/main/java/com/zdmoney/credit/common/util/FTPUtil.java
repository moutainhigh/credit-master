package com.zdmoney.credit.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPUtil {
    
    private FTPClient ftpClient;

    public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;
    
    public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;

    //传输超时时间
    public static int dateTimes = 1200000;
    //连接超时时间
    public static int connectTimes = 20000;
    
    public void connectServer(String host, String port, String user,String password, String remotePath) throws SocketException,IOException {
        connectServer(host, Integer.parseInt(port), user, password, remotePath);
    }

    /**
     * 连接远程FTP服务器
     * @param server
     * @param port
     * @param user
     * @param password
     * @param path
     * @throws SocketException
     * @throws IOException
     */
    public void connectServer(String server, int port, String user,String password, String path) throws SocketException, IOException {
        ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        System.out.println("Connected to " + server + ".");
        System.out.println(ftpClient.getReplyCode());
        ftpClient.login(user, password);
        // 设置被传输文件的编码类型
        ftpClient.setFileType(BINARY_FILE_TYPE);
        // 设置输出文件的传输模式
        ftpClient.setFileTransferMode(BINARY_FILE_TYPE);
        if (path.length() != 0) {
            ftpClient.changeWorkingDirectory(path);
        }
    }
    public void connectServer(String host, String port, String user,String password, String remotePath,int dateTimes,int connectTimes) throws SocketException,IOException {
        connectServer(host, Integer.parseInt(port), user, password, remotePath,dateTimes,connectTimes);
    }
    public void connectServer(String server, int port, String user,String password, String path,int dateTimes,int connectTimes) throws SocketException, IOException {
        ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        System.out.println("Connected to " + server + ".");
        System.out.println(ftpClient.getReplyCode());
        ftpClient.login(user, password);
        // 设置被传输文件的编码类型
        ftpClient.setFileType(BINARY_FILE_TYPE);
        // 设置输出文件的传输模式
        ftpClient.setFileTransferMode(BINARY_FILE_TYPE);
        //设置传输超时时间
        ftpClient.setDataTimeout(dateTimes);
        //连接超时
        ftpClient.setConnectTimeout(connectTimes);
        ftpClient.enterLocalActiveMode();
        if (path.length() != 0) {
            ftpClient.changeWorkingDirectory(path);
        }
    }


    public void setFileType(int fileType) throws IOException {
        ftpClient.setFileType(fileType);
    }

    public void closeServer() throws IOException {
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
    }

    public boolean changeDirectory(String path) throws IOException {
        return ftpClient.changeWorkingDirectory(path);
    }

    public boolean createDirectory(String pathName) throws IOException {
        return ftpClient.makeDirectory(pathName);
    }

    public boolean removeDirectory(String path) throws IOException {
        return ftpClient.removeDirectory(path);
    }

    public boolean removeDirectory(String path, boolean isAll)throws IOException {
        if (!isAll) {
            return removeDirectory(path);
        }

        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr == null || ftpFileArr.length == 0) {
            return removeDirectory(path);
        }
        for (FTPFile ftpFile : ftpFileArr) {
            String name = ftpFile.getName();
            if (ftpFile.isDirectory()) {
                System.out.println("* [sD]Delete subPath [" + path + "/" + name + "]");
                removeDirectory(path + "/" + name, true);
            } else if (ftpFile.isFile()) {
                System.out.println("* [sF]Delete file [" + path + "/" + name + "]");
                deleteFile(path + "/" + name);
            } else if (ftpFile.isSymbolicLink()) {

            } else if (ftpFile.isUnknown()) {

            }
        }
        return ftpClient.removeDirectory(path);
    }

    /**
     * 检查文件路径是否存在，存在返回true，否则返回false
     * @param path
     * @return
     * @throws IOException
     */
    public boolean existDirectory(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        for (FTPFile ftpFile : ftpFileArr) {
            if (ftpFile.isDirectory() && ftpFile.getName().equalsIgnoreCase(path)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    
    /**
     * 被动模式检查文件路径是否存在，存在返回true，否则返回false
     * @param path
     * @return
     * @throws IOException
     */
    public boolean existDirectory4Pasv(String path) throws IOException {
        boolean flag = false;
        ftpClient.enterLocalPassiveMode();
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        for (FTPFile ftpFile : ftpFileArr) {
            if (ftpFile.isDirectory() && ftpFile.getName().equalsIgnoreCase(path)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    
    public List<String> getFileList(String path) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles(path);
        List<String> retList = new ArrayList<String>();
        if (ftpFiles == null || ftpFiles.length == 0) {
            return retList;
        }
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.isFile()) {
                retList.add(ftpFile.getName());
            }
        }
        return retList;
    }

    public boolean deleteFile(String pathName) throws IOException {
        return ftpClient.deleteFile(pathName);
    }

    public boolean uploadFile(String fileName, String newName)throws IOException {
        boolean flag = false;
        InputStream iStream = null;
        try {
            iStream = new FileInputStream(fileName);
            flag = ftpClient.storeFile(newName, iStream);
        } catch (IOException e) {
            flag = false;
            return flag;
        } finally {
            if (iStream != null) {
                iStream.close();
            }
        }
        return flag;
    }

    public boolean uploadFile(String fileName) throws IOException {
        return uploadFile(fileName, fileName);
    }

    public boolean uploadFile(InputStream iStream, String newName) throws IOException {
        boolean flag = false;
        try {
            flag = ftpClient.storeFile(newName, iStream);
        } catch (IOException e) {
            flag = false;
            return flag;
        } finally {
            if (iStream != null) {
                iStream.close();
            }
        }
        return flag;
    }

    public boolean download(String remoteFileName, String localFileName) throws IOException {
        boolean flag = false;
        File outfile = new File(localFileName);
        OutputStream oStream = null;
        try {
            oStream = new FileOutputStream(outfile);
            flag = ftpClient.retrieveFile(remoteFileName, oStream);
        } catch (IOException e) {
            flag = false;
            return flag;
        } finally {
            oStream.close();
        }
        return flag;
    }
    
    public boolean download(String remoteFileName, OutputStream out) throws IOException {
		boolean flag = false;
		try {
			flag = ftpClient.retrieveFile(remoteFileName, out);
		} catch (IOException e) {
			flag = false;
			return flag;
		} finally {

		}
		return flag;
	}

    public boolean downloadFtp(String remoteFileName) throws IOException {
        boolean flag = false;
        File outfile = new File(remoteFileName);
        OutputStream oStream = null;
        try {
            oStream = new FileOutputStream(outfile);
            flag = ftpClient.retrieveFile(remoteFileName, oStream);
        } catch (IOException e) {
            flag = false;
            return flag;
        } finally {
            oStream.close();
        }
        return flag;
    }

    public InputStream downFile(String sourceFileName) throws IOException {
        return ftpClient.retrieveFileStream(sourceFileName);
    }

    public List<String> getDicFileList(String path) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles(path);
        List<String> retList = new ArrayList<String>();
        if (ftpFiles == null || ftpFiles.length == 0) {
            return retList;
        }
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.isDirectory()) {
                retList.add(ftpFile.getName());
            }
        }
        return retList;
    }

    /**
     * 判断目个目录下是否存在目个子目录
     * @param parentPath
     * @param tarPath
     * @return
     * @throws IOException
     */
    public boolean isChildDirectory(String parentPath,String tarPath) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(parentPath);
        for (FTPFile ftpFile : ftpFileArr) {
            System.out.println("是否目录：" + ftpFile.isDirectory() + ":目录:" + ftpFile.getName());
            if (ftpFile.isDirectory() && ftpFile.getName().equalsIgnoreCase(tarPath)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    /**
     * 检查文件是否存在，存在返回true，否则返回false
     * @param path
     * @return
     * @throws IOException
     */
    public boolean existFile(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        for (FTPFile ftpFile : ftpFileArr) {
            if (ftpFile.isFile() && ftpFile.getName().equalsIgnoreCase(path)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     *按照顺序获取文件名
     * @param path
     * @return
     * @throws IOException
     */
    public List<String> getFileListAsc(String path) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles(path);
        List<String> retList = new ArrayList<String>();
        if (ftpFiles == null || ftpFiles.length == 0) {
            return retList;
        }
        for (FTPFile ftpFile : ftpFiles) {
                Arrays.sort(ftpFiles,new Comparator< FTPFile>(){
                    @Override
                    public int compare(FTPFile f1, FTPFile f2) {
                        long diff =  f1.getTimestamp().getTimeInMillis() - f2.getTimestamp().getTimeInMillis();
                        if (diff > 0)
                            return 1;
                        else if (diff == 0)
                            return 0;
                        else
                            return -1;
                    }
                });

            if (ftpFile.isFile()) {
                    retList.add(ftpFile.getName());
                }
            }
        return retList;
    }
    
    /**
     * 创建并且更改文件路径
     * @param path
     * @return
     * @throws IOException
     */
    public boolean createAndChangeDir(String path) throws IOException{
    	boolean res = true;
    	if(!ftpClient.changeWorkingDirectory(path)){
    		ftpClient.makeDirectory(path);
    		res = ftpClient.changeWorkingDirectory(path);
    	}
    	return res;
    }

    public void enterLocalPassiveMode(){
    	ftpClient.enterLocalPassiveMode();
    }
    
    public void enterLocalActiveMode(){
    	ftpClient.enterLocalActiveMode();
    }
    
	/**
	 * @param filePath
	 * @throws IOException 
	 */
	public boolean createDeepFilePaths(String filePaths) throws IOException {
		boolean res = true;
		String[] filePath = filePaths.split("/");
		StringBuffer fp = new StringBuffer();
		for(String path:filePath){
			if(Strings.isEmpty(path)){
				continue;
			}
			fp.append("/").append(path);
			if(!existDirectory(fp.toString())){
				createDirectory(fp.toString());
			}
		}
		return res;
	}
	
	/**
	 * 被动模式创建文件路劲
	 * @param filePath
	 * @throws IOException 
	 */
	public boolean createDeepFilePaths4Pasv(String filePaths) throws IOException {
		boolean res = true;
		String[] filePath = filePaths.split("/");
		StringBuffer fp = new StringBuffer();
		for(String path:filePath){
			if(Strings.isEmpty(path)){
				continue;
			}
			fp.append("/").append(path);
//			if(!existDirectory4Pasv(fp.toString())){
				createDirectory(fp.toString());
//			}
		}
		return res;
	}
	
	
    public static void main(String args[]) throws SocketException, IOException{
    	FTPUtil ftp = new FTPUtil();
    	ftp.connectServer("172.16.250.69", "21", "qianzhang", "!qaz@wsx","/aps/20160520170000194611/PDF SEREAL");
        System.out.print(ftp.existFile("text.txt"));
    }
}