package com.nayan.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

import com.nayan.logger.ThreadLogger;

public class FileUtil
{
   
    private static String rootDir;

    /**
     * generate a checksum value for a file
     * @param f
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static long getChecksum(File f) throws FileNotFoundException, IOException
    {

        CheckedInputStream cis = new CheckedInputStream(new FileInputStream(f), new Adler32());
        byte[] tempBuf = new byte[128];
        while (cis.read(tempBuf) >= 0)
        {
        }
        return cis.getChecksum().getValue();

    }
    
    /**
     * Copies from an InputStream and writes the entries stream
     * to an OutputStream.
     * @param is The input stream to read from
     * @param os The output stream to write to
     * @return The number of bytes (read/written) between the streams
     * @throws java.io.IOException
     */
    public static long copy(InputStream is, OutputStream os) throws IOException
    {
        byte buffer[] = new byte[1024];
        int bytesRead;
        long totalCopied = 0;
        do
        {
            bytesRead = is.read(buffer);
            if (bytesRead > 0)
            {
                os.write(buffer, 0, bytesRead);
                totalCopied += bytesRead;
            }
        } while ((bytesRead > 0));
        return totalCopied;
    }
    
    public static void copyNewFile(String _sourceFile, String _destFile) throws FileNotFoundException, IOException
    {
        File destCheck = new File(_destFile);
        File parentDir = new File(destCheck.getParent());

        if (!parentDir.exists() || !parentDir.isDirectory())
        {
            parentDir.mkdirs();
        }
        File dest = new File(_destFile );
        File orig = new File(_sourceFile);

        FileInputStream from = null;
        FileOutputStream to = null;

        from = new FileInputStream(orig);
        to = new FileOutputStream(dest);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = from.read(buffer)) != -1)
        {
            to.write(buffer, 0, bytesRead); // write

        }
        from.close();
        to.close();
        dest.setLastModified(orig.lastModified());

    }
    
    
    /**
     * copy a file from source to destination
     * @param _sourceFile
     * @param _destFile
     * @param _source
     * @param _dest
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void copyNewFile(String _sourceFile, String _destFile, String _source, String _dest) throws FileNotFoundException, IOException
    {              
        copyNewFile(_sourceFile + File.separator + _source, _destFile + File.separator + _dest);
    }

    
    /**
     * convert contents of file to string
     * @param filePath
     * @return
     * @throws java.io.IOException 
     */
    public static String fileToString(String filePath)
      throws java.io.IOException
    {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(
          new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1)
        {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    
    public static byte[] fileToByteArray(File file) throws IOException
    {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE)
        {
            throw new IOException("File Size is too large to convert to byte array");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
        {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length)
        {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();


        return bytes;
    }


    /**
     * This method checks whether the given directory exists in the file system. If it does not, it tries to create it.
     *
     * @param directoryPath the directory to be checked, i.e. complete path to it
     * @return <code>true</code> if the directory exists; <code>false</code> otherwise, including failure trying to create it
     */
    public static boolean assertDirectoryExists(String directoryPath) {
        try {
            if (directoryPath == null || directoryPath.length() == 0) {
                return false;
            }

            // test whether the directory exists AND is indeed a directory
            File directory = new File(directoryPath);
            if (!directory.isDirectory()) {
                // try to create the directory
                directory.mkdirs();
            }

            return directory.isDirectory();
        } catch (Exception e) {
            ThreadLogger.getLogger(FileUtil.class).error(e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
    }
    
    
    public static String concatToFileName(String... files)
    {
        if (files == null || files.length == 0)
            return null;
        
        StringBuilder sb = new StringBuilder();
        for (String file : files)
        {
            if (file == null || file.length() == 0)
                continue;
            
            if (sb.length() > 0 && !sb.toString().endsWith(File.separator))
                sb.append(File.separator);

            sb.append(file);             
        }
        
        return sb.toString();
    }
    
    
    /**
     * get root dir of filesystem
     * e.g. on windows "C:" or "D:"
     * on unix "/"
     * @return 
     */
    public static synchronized String getRoot()
    {
        if (rootDir != null)
            return rootDir;
        
        try
        {
            File d = new File(".");
            String dir = d.getCanonicalPath();

            int dosDir = dir.indexOf("\\");
            int unixDir = dir.indexOf("/");
            dosDir = dosDir == -1? dir.length()+1 : dosDir;
            unixDir = unixDir == -1? dir.length()+1 : unixDir;

            rootDir = dir.substring(0, Math.min(dosDir, unixDir) + 1);
            
            return rootDir;
        
        }
        catch (Exception e)
        {
            return "/";
        }
    }
    
    
    public static void checkDirAccess(String filename, boolean canWrite) throws Exception
    {
        File dir = new File(filename);
        if (!dir.exists())
        {
            throw new Exception("cannot access directory " + filename);
        }
        
        if (!dir.isDirectory())
        {
            throw new Exception("directory " + filename + " is not recognised as a directory");
        }
        
        if (!dir.canRead())
        {
            throw new Exception("directory " + filename + " is not readable");
        }

        if (canWrite && !dir.canWrite())
        {
            throw new Exception("directory " + filename + " is not writeable");
        }

    }

    
    public static void checkFileAccess(String filename, boolean canWrite) throws Exception
    {
        File file = new File(filename);
        if (!file.exists())
        {
            throw new Exception("cannot access file " + filename);
        }
        
        if (!file.isFile())
        {
            throw new Exception("file " + filename + " is not recognised as a file");
        }
        
        if (!file.canRead())
        {
            throw new Exception("file " + filename + " is not readable");
        }

        if (canWrite && !file.canWrite())
        {
            throw new Exception("file " + filename + " is not writeable");
        }

    }

    public static String getExtension(String file)
    {
        String extension = "";
        int i = file.lastIndexOf('.');
        if (i > 0)
        {
            extension = file.substring(i + 1);
        }
        return extension;
    }
 
    public static void copyFolder(File src, File dest, boolean moveSubDir) throws IOException {
 
    	if(!src.isDirectory())
            throw new IOException(src.getAbsolutePath() + " is not a directory");
 
        //if directory not exists, create it
        if(!dest.exists()){
           dest.mkdir();
        }

        //list all the directory contents
        String files[] = src.list();

        for (String file : files) {
           //construct the src and dest file structure
           File srcFile = new File(src, file);
           File destFile = new File(dest, file);

           if (srcFile.isDirectory() && moveSubDir) 
               Files.move(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
           else if (!srcFile.isDirectory()) 
               Files.move(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
           
        }
    }

}
