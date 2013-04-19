package services.pub;

import java.io.*;
import java.util.ArrayList;

public class FileFun
{

    public FileFun()
    {
    }

    public boolean createAndDeleteFolder(String s, String s1)
    {
        boolean flag = false;
        try
        {
            File file = new File(s1 + s);
            if(!file.exists())
            {
                file.mkdirs();
                flag = true;
            } else
            {
                flag = true;
            }
        }
        catch(Exception exception)
        {
            flag = false;
            System.out.println("CreateAndDeleteFolder is error:" + exception);
        }
        return flag;
    }

    public void readFolderByFile(String s)
    {
        File file = new File(s);
        File afile[] = file.listFiles();
        for(int i = 0; i < afile.length; i++)
        {
            if(afile[i].isFile())
                System.out.println("File : " + afile[i].getName());
            if(afile[i].isDirectory())
                System.out.println("Directory : " + afile[i].getName());
        }

    }

    public static void move(String s, String s1)
        throws Exception
    {
        File file = new File(s);
        File file1 = new File(s1);
        file.renameTo(file1);
    }

    public static boolean copy(String s, String s1)
        throws Exception
    {
        int i = 0x10000;
        try
        {
            FileInputStream fileinputstream = new FileInputStream(s);
            FileOutputStream fileoutputstream = new FileOutputStream(s1);
            byte abyte0[] = new byte[i];
            int j;
            while((j = fileinputstream.read(abyte0)) > -1) 
                fileoutputstream.write(abyte0, 0, j);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return true;
    }

    public static void CopyDir(String s, String s1)
        throws Exception
    {
        File file = new File(s1);
        File file1 = new File(s);
        String as[] = file1.list();
        try
        {
            file.mkdirs();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        for(int i = 0; i < as.length; i++)
        {
            String s2 = file1 + File.separator + as[i];
            String s3 = file + File.separator + as[i];
            File file2 = new File(s2);
            if(!file2.isFile())
                continue;
            try
            {
                copy(s2, s3);
            }
            catch(Exception exception1)
            {
                exception1.printStackTrace();
            }
        }

    }

    public static void recursiveRemoveDir(File file)
        throws Exception
    {
        if(!file.exists())
            throw new IOException(file.toString() + "do not exist!");
        String as[] = file.list();
        Object obj = null;
        for(int i = 0; i < as.length; i++)
        {
            File file1 = new File(file.getAbsolutePath(), as[i]);
            if(file1.isDirectory())
            {
                recursiveRemoveDir(file1);
                continue;
            }
            if(file1.isFile())
                file1.delete();
        }

        file.delete();
    }

    public String replaceStr(String s, String s1, String s2)
    {
        String s3 = "";
        int i = s1.length();
        int j;
        while((j = s.indexOf(s1)) != -1) 
        {
            s3 = s3 + s.substring(0, j);
            s3 = s3 + s2;
            s = s.substring(j + i);
        }
        s3 = s3 + s;
        return s3;
    }

    public ArrayList readfile(String s, String s1)
    {
        BufferedReader bufferedreader = null;
        Object obj = null;
        ArrayList arraylist = new ArrayList();
        try
        {
            String as[];
            for(bufferedreader = new BufferedReader(new FileReader(s1)); bufferedreader.read() != -1; arraylist.add(as))
            {
                String s2 = bufferedreader.readLine();
                as = s2.split("\\" + s);
            }

        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            try
            {
                if(bufferedreader != null)
                    bufferedreader.close();
            }
            catch(Exception exception2)
            {
                exception2.printStackTrace();
            }
        }
        return arraylist;
    }
}