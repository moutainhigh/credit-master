package com.zdmoney.credit.common.util;


    import java.io.BufferedInputStream;  
    import java.io.BufferedOutputStream;  
    import java.io.File;  
    import java.io.FileInputStream;  
    import java.io.FileOutputStream;  
    import java.io.IOException;  
    import java.io.InputStream;  
    import java.io.OutputStream;  
    import java.util.ArrayList;  
    import java.util.List;  
      
    import org.apache.commons.compress.archivers.ArchiveEntry;  
    import org.apache.commons.compress.archivers.zip.Zip64Mode;  
    import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;  
    import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;  
    import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;  
      
    public class CommonsCompressZipUtils {
      
        /**
         * 
        * @Title: zip 
        * @Description:             压缩zip文件   
        * @param @param dir         原路径      如 D:\\workspace\\runtime-com.qz.meta.product\\xxxx\\.image
        * @param @param zippath     目标路径  如 D:\\test2.zip"
        * @param @throws IOException
        * @return void
        * @throws
         */
        public static void zip(String dir ,String zippath) throws IOException{  
            List<String> paths = getFiles(dir);   
            compressFilesZip(paths.toArray(new String[paths.size()]),zippath,dir);  
        }  
        /**
         *   
        * @Title: getFiles 
        * @Description: 递归取到当前目录所有文件  
        * @param @param dir
        * @return List<String>
        * @throws
         */
        public static List<String> getFiles(String dir){  
            List<String> lstFiles = null;       
            if(lstFiles == null){  
                lstFiles = new ArrayList<String>();  
            }  
            File file = new File(dir);  
            File [] files = file.listFiles();   
            for(File f : files){  
                if(f.isDirectory()){  
                    lstFiles.add(f.getAbsolutePath());  
                    lstFiles.addAll(getFiles(f.getAbsolutePath()));  
                }else{   
                    String str =f.getAbsolutePath();  
                    lstFiles.add(str);  
                }  
            }  
            return lstFiles;  
        }  
          
        /**
         *  
        * @Title: getFilePathName 
        * @Description: 文件名处理 
        * @param @param dir
        * @param @param path
        * @return String
        * @throws
         */
        public static String getFilePathName(String dir,String path){  
            String p = path.replace(dir+File.separator, "");  
            p = p.replace("\\", "/");  
            return p;  
        }  
        /**
         *   
        * @Title: compressFilesZip 
        * @Description:                 把文件压缩成zip格式 
        * @param @param files           需要压缩的文件
        * @param @param zipFilePath     压缩后的zip文件路径   ,如"D:/test/aa.zip"
        * @param @param dir
        * @param @throws IOException
        * @return void    返回类型 
        * @throws
         */
        public static void compressFilesZip(String[] files,String zipFilePath,String dir) throws IOException {  
            if(files == null || files.length <= 0) {  
                return ;  
            }  
            ZipArchiveOutputStream zaos = null;  
            try {  
                File zipFile = new File(zipFilePath);  
                zaos = new ZipArchiveOutputStream(zipFile);  
                zaos.setUseZip64(Zip64Mode.AsNeeded);  
                //将每个文件用ZipArchiveEntry封装  
                //再用ZipArchiveOutputStream写到压缩文件中  
                for(String strfile : files) {  
                    File file = new File(strfile);  
                    if(file != null) {  
                        String name = getFilePathName(dir,strfile);  
                        ZipArchiveEntry zipArchiveEntry  = new ZipArchiveEntry(file,name);  
                        zaos.putArchiveEntry(zipArchiveEntry);  
                        if(file.isDirectory()){  
                            continue;  
                        }  
                        InputStream is = null;  
                        try {  
                            is = new BufferedInputStream(new FileInputStream(file));  
                            byte[] buffer = new byte[1024 ];   
                            int len = -1;  
                            while((len = is.read(buffer)) != -1) {  
                                //把缓冲区的字节写入到ZipArchiveEntry  
                                zaos.write(buffer, 0, len);  
                            }  
                            zaos.closeArchiveEntry();   
                        }catch(Exception e) {  
                            throw new RuntimeException(e);  
                        }finally {  
                            if(is != null)  
                                is.close();  
                        }  
                           
                    }  
                }  
                zaos.finish();  
            }catch(Exception e){  
                throw new RuntimeException(e);  
            }finally {  
                    try {  
                        if(zaos != null) {  
                            zaos.close();  
                        }  
                    } catch (IOException e) {  
                        throw new RuntimeException(e);  
                    }  
            }  
               
        }  
          
        /**
         *   
        * @Title: unzip 
        * @Description:                 把zip文件解压到指定的文件夹 
        * @param @param zipFilePath     zip文件路径, 如 "D:/test/aa.zip" 
        * @param @param saveFileDir     解压后的文件存放路径, 如"D:/test/"
        * @param @throws IOException
        * @return void
        * @throws
         */
        public static void unzip(String zipFilePath, String saveFileDir) throws IOException {  
            if(!saveFileDir.endsWith("\\") && !saveFileDir.endsWith("/") ){  
                saveFileDir += File.separator;  
            }  
            File dir = new File(saveFileDir);  
            if(!dir.exists()){  
                dir.mkdirs();  
            }  
            File file = new File(zipFilePath);  
            if (file.exists()) {  
                InputStream is = null;   
                ZipArchiveInputStream zais = null;  
                try {  
                    is = new FileInputStream(file);  
                    zais = new ZipArchiveInputStream(is);  
                    ArchiveEntry archiveEntry = null;  
                    while ((archiveEntry = zais.getNextEntry()) != null) {   
                        // 获取文件名  
                        String entryFileName = archiveEntry.getName();  
                        // 构造解压出来的文件存放路径  
                        String entryFilePath = saveFileDir + entryFileName;  
                        OutputStream os = null;  
                        try {  
                            // 把解压出来的文件写到指定路径  
                            File entryFile = new File(entryFilePath);  
                            if(entryFileName.endsWith("/")){  
                                entryFile.mkdirs();  
                            }else{  
                                os = new BufferedOutputStream(new FileOutputStream(  
                                        entryFile));                              
                                byte[] buffer = new byte[1024 ];   
                                int len = -1;   
                                while((len = zais.read(buffer)) != -1) {  
                                    os.write(buffer, 0, len);   
                                }  
                            }  
                        } catch (IOException e) {  
                            throw new IOException(e);  
                        } finally {  
                            if (os != null) {  
                                os.flush();  
                                os.close();  
                            }  
                        }  
      
                    }   
                } catch (Exception e) {  
                    throw new RuntimeException(e);  
                } finally {  
                    try {  
                        if (zais != null) {  
                            zais.close();  
                        }  
                        if (is != null) {  
                            is.close();  
                        }  
                    } catch (IOException e) {  
                        throw new RuntimeException(e);  
                    }  
                }  
            }  
        }  
    }

