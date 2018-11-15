import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

public class TestDistanceCalculator {

    final static String FILE_NAME = "testfile.txt";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();


    private DistanceCalculator initDistanceCalculator(String word1, String word2, String content) throws IOException {
        final File tempFile;
        tempFile = tempFolder.newFile(FILE_NAME);
        // Write something to it.
        FileUtils.writeStringToFile(tempFile, content);
        // Read it from temp file
        final String s = FileUtils.readFileToString(tempFile);
        //System.out.println("File to string: " + tempFile);
        return new DistanceCalculator(tempFile.getAbsolutePath(), word1, word2);
    }



    @Test
    public void testMissingWords() throws IOException {
        DistanceCalculator dc = initDistanceCalculator("", "", "qwe zxc asd");
        Assert.assertEquals(0, dc.getNearestDistance());
        Assert.assertEquals(0, dc.getLongestDistance());
    }

    @Test
    public void testEmptyText() throws IOException {
        DistanceCalculator dc = initDistanceCalculator("asd", "zxc", "");
        Assert.assertEquals(0, dc.getNearestDistance());
        Assert.assertEquals(0, dc.getLongestDistance());
    }

    @Test
    public void testSameWordsInSingleWordFile() throws IOException {
        DistanceCalculator dc = initDistanceCalculator("zxc", "zxc", "zxc");
        Assert.assertEquals(0, dc.getNearestDistance());
        Assert.assertEquals(0, dc.getLongestDistance());
    }

    @Test
    public void testSameWordsInMultiWordFile() throws IOException {
        DistanceCalculator dc = initDistanceCalculator("zxc", "zxc", "zxc zxc zxc asd");
        Assert.assertEquals(0, dc.getNearestDistance());
        Assert.assertEquals(0, dc.getLongestDistance());
    }

    @Test
    public void testSimpleWordsNear() throws IOException {
        DistanceCalculator dc = initDistanceCalculator("qwe", "asd", "qwe asd");
        Assert.assertEquals(0, dc.getNearestDistance());
        Assert.assertEquals(0, dc.getLongestDistance());
    }

    @Test
    public void testSimpleWordsWithLittleDistance() throws IOException {
        DistanceCalculator dc = initDistanceCalculator("qwe", "asd", "qwe zxc asd");
        Assert.assertEquals(1, dc.getNearestDistance());
        Assert.assertEquals(1, dc.getLongestDistance());
    }

    @Test
    public void testSimpleWordsWithMultipleOccurences() throws IOException {
        DistanceCalculator dc = initDistanceCalculator("qwe", "asd", "qwe zxc asd asd");
        Assert.assertEquals(1, dc.getNearestDistance());
        Assert.assertEquals(2, dc.getLongestDistance());
    }

    @Test
    public void testSimpleWordsWithMultipleOccurencesDuplicateSpaces() throws IOException {
        DistanceCalculator dc = initDistanceCalculator("qwe", "asd", "  qwe  zxc  asd       asd");
        Assert.assertEquals(1, dc.getNearestDistance());
        Assert.assertEquals(2, dc.getLongestDistance());
    }

    @Test
    public void testVeryLongSequence() throws IOException {
        String content = "qwe zxc asd asd";
        for (int i = 0; i < 10000; ++i) {
            content += " asd";
        }

        DistanceCalculator dc = initDistanceCalculator("qwe", "asd", content);
        Assert.assertEquals(1, dc.getNearestDistance());
        Assert.assertEquals(10002, dc.getLongestDistance());
    }
}
