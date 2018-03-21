
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class WordCount{

    String filePath;
    String fileName;
    String stopList;
    String buffer;
    List<File> fileList;
    static int MAX_LINES = 4096;
    WordCount()
    {
        buffer = new String();
        fileList = new ArrayList<>();
    }
    WordCount(String fileName)
    {

        this.fileName = fileName;
        buffer = new String();
        fileList = new ArrayList<>();
    }
    public void readFile(String absolutePath) {

        File file = new File(absolutePath);
        buffer = "";
        try {
            InputStream inputStream = new FileInputStream(file);
            int size = inputStream.available();

            for(int i = 0; i < size; i++)
            {
                //buffer[i] = (char)inputStream.read();

            buffer += (char)inputStream.read();
        }

        } catch (IOException e) {
            System.out.println(e);
        }

        //System.out.println(buffer);

    }

    public void writeFile(String fileName, String content)
    {
        File root = new File("");
        OutputStream fos;
        try
        {
            fos = new FileOutputStream(root.getAbsolutePath() + "\\" + fileName);
            OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
            content = content.replace("\n", "\r\n");
            writer.append(content);
            writer.close();
            fos.close();
        }catch (Exception e)
        {
            System.out.println(e);
        }


    }

    public void setStopListFile(String filename)
    {
        File root = new File("");
        //System.out.println(root.getAbsolutePath());
        File file = new File(root.getAbsolutePath() + '\\' + filename);

        try {
            InputStream inputStream = new FileInputStream(file);
            int size = inputStream.available();

            for(int i = 0; i < size; i++)
            {


                stopList += (char)inputStream.read();
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }
    public int countChar()
    {
        return buffer.length();
    }

    public int countLine()
    {
        if(buffer.length() == 0)
            return 0;

        String[] lines = buffer.split("\n", -1);
        return lines.length;
    }
    public int countWord()
    {
        if(buffer.length() == 0)
            return 0;

        String[] words = buffer.split("[, \t\n]+");
        return words.length;
    }

    public int countCodeLine()
    {
        if(buffer.length() == 0)    return 0;

        String[] lines = buffer.split("\n", -1);

        return lines.length - countCommentLine() - countEmptyLine();
    }

    public int countEmptyLine()
    {
        if(buffer.length() == 0)    return 0;
        String regex = "[\\s]*[\\S]?[\\s]*";
        Pattern emptyLinePattern = Pattern.compile(regex);

        String[] lines = buffer.split("\n", -1);

        Matcher matcher;
        int result = 0;
        for(String line : lines)
        {
            matcher = emptyLinePattern.matcher(line);
            if(matcher.matches())
                result++;
        }
        return result;

    }

    public int countCommentLine()
    {
        if(buffer.length() == 0)    return 0;

        String temp = "";
        String regex = "(//.*\\n)|(/\\*(.|\\n)*?\\*/[\\n]?)";
        Pattern commentPattern = Pattern.compile(regex);
        Matcher matcher = commentPattern.matcher(buffer);

        while(matcher.find())
        {
            //temp += matcher.group();
            System.out.println(matcher.group());
        }
        if(temp.equals("")) return 0;
        //System.out.print(temp);
        return temp.split("\\n").length;
    }

    public int countWordWithStopList()
    {
        if(buffer.length() == 0)    return 0;

        String[] stopWords = stopList.split("[ ]");
        List<String> stopWordList = Arrays.asList(stopWords);
        String[] words = buffer.split("[, \t\n]+");

        int result = words.length;

        for(String word : words)
        {
            if(stopWordList.contains(word))
            {
                result--;
            }
        }

        return result;
    }

    public void getFileList(String path)//传入绝对路径
    {
        //*.c // A/B/C/*.c

        String searchPath = new String();
        String filePattern = new String();

        Pattern single = Pattern.compile("\\*\\.\\S+");
        Matcher singleMatcher = single.matcher(path);

        Pattern pathPattern = Pattern.compile("\\S+\\*\\.\\S+");
        Matcher pathMatcher = pathPattern.matcher(path);

        if(singleMatcher.matches())
        {
            searchPath = (new File("").getAbsolutePath());
            filePattern = path.substring(path.indexOf('*'));
        }
        if(pathMatcher.matches())
        {
            searchPath = path.substring(0, path.indexOf('*'));
            filePattern = path.substring(path.indexOf('*'));
        }

        File root = new File(searchPath);

        for(File fl : root.listFiles())
        {

            if(fl.isFile())
            {
                if(Pattern.compile(filePattern.replace("*.", "[a-zA-z0-9]+\\.")).matcher(fl.getName()).matches())
                    fileList.add(fl);

            }
            if(fl.isDirectory())
            {
                String temp = fl.getAbsolutePath() + "\\" + filePattern;
                //System.out.println(temp);
                getFileList(temp);
            }


        }

    }






    public static void main(String args[]) throws Exception
    {
        boolean hasOptionA = false;
        boolean hasOptionC = false;;
        boolean hasOptionW = false;;
        boolean hasOptionL = false;;
        boolean hasOptionS = false;;
        boolean hasOptionE = false;;
        boolean hasOptionO = false;;
        String outputString = "";

        String fileParameter = "";

        String oParameter = "";
        String eParameter = "";
        for(int i = 0; i < args.length; i++)
        {
            String str = args[i];

            if (str.equals("-a")) hasOptionA = true;
            else if (str.equals("-c")) hasOptionC = true;
            else if (str.equals("-w")) hasOptionW = true;
            else if (str.equals("-l")) hasOptionL = true;
            else if (str.equals("-s")) hasOptionS = true;
            else if (str.equals("-e"))
            {
                hasOptionE = true;
                eParameter = args[i+1];
                i++;
            }

            else if (str.equals("-o"))
            {
                hasOptionO = true;
                oParameter = args[i+1];
                i++;
            }

            else
            {
                fileParameter = str;
            }
        }


        WordCount wordCount = new WordCount();




        if(hasOptionS){

            wordCount.getFileList(fileParameter);

            for(File f : wordCount.fileList) {

                wordCount.readFile(f.getAbsolutePath());
                wordCount.fileName = f.getName();

                if (hasOptionC) {
                    outputString += (wordCount.fileName + "," + "字符数：" + wordCount.countChar() + "\n");
                }

                if (hasOptionW && !hasOptionE) {
                    outputString += (wordCount.fileName + "," + "单词数：" + wordCount.countWord() + "\n");
                }

                if (hasOptionL) {
                    outputString += (wordCount.fileName + "," + "行数：" + wordCount.countLine() + "\n");
                }

                if (hasOptionE) {
                    wordCount.setStopListFile(eParameter);
                    outputString += (wordCount.fileName + "," + "单词数：" + wordCount.countWordWithStopList() + "\n");
                }
                if (hasOptionA) {
                    outputString += wordCount.fileName + "," + "代码行/空行/注释行：" + wordCount.countCodeLine() + "/" + wordCount.countEmptyLine() + "/" + wordCount.countCommentLine() + "\n";
                }
            }

        }
        else
        {
            wordCount.fileName = fileParameter;
            File root = new File("");
            String absolutePath = root.getAbsolutePath() + "/" + fileParameter;
            wordCount.readFile(absolutePath);
            wordCount.fileName = new File(absolutePath).getName();
            //System.out.println(fileParameter);
            if (hasOptionC) {
                outputString += (wordCount.fileName + "," + "字符数：" + wordCount.countChar() + "\n");
            }

            if (hasOptionW && !hasOptionE) {
                outputString += (wordCount.fileName + "," + "单词数：" + wordCount.countWord() + "\n");
            }

            if (hasOptionL) {
                outputString += (wordCount.fileName + "," + "行数：" + wordCount.countLine() + "\n");
            }

            if (hasOptionE) {
                wordCount.setStopListFile(eParameter);
                outputString += (wordCount.fileName + "," + "单词数：" + wordCount.countWordWithStopList() + "\n");
            }
            if (hasOptionA) {
                outputString += wordCount.fileName + "," + "代码行/空行/注释行：" + wordCount.countCodeLine() + "/" + wordCount.countEmptyLine() + "/" + wordCount.countCommentLine() + "\n";
            }
        }
        if(hasOptionO)
        {
            wordCount.writeFile(oParameter, outputString);
        }
        else
        {
            wordCount.writeFile("result.txt", outputString);
            System.out.print(outputString);
        }


    }

}
