import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Vaka implements Runnable {

    public static Map<String, Integer> prop = new HashMap<>();

    public static void main(String[] args) {
        Vaka vaka = new Vaka();
        Thread mainThread = new Thread(vaka);
        mainThread.start();

    }

    public static TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> map){
        Comparator<String> comparator = new ValueComparator(map);
        TreeMap<String, Integer> result = new TreeMap<>(comparator);
        result.putAll(map);
        return result;
    }

    @Override
    public void run() {
        try {
            mainProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mainProcess() throws Exception {
        File file = new File("input.txt");
        FileReader fileReader = new FileReader(file);
        String text = "";
        String line;
        BufferedReader br = new BufferedReader(fileReader);

        while ((line = br.readLine()) != null) {
            text += line;
        }
        br.close();
        String[] sentenceHolder = text.split("(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\.|\\?)\\s");
        Thread[] threadArr = new Thread[sentenceHolder.length];
        int i = 0;
        int countSentences = sentenceHolder.length;
        int avgSentences = 0;
        for (String sentence : sentenceHolder) {
            String[] word = sentence.split(" ");
            avgSentences += word.length;
            MyRunnable myRunnable = new MyRunnable(sentence.substring(0, sentence.length() - 1));
            threadArr[i] = new Thread(myRunnable);
            threadArr[i].start();

            i++;
        }

        System.out.println("Sentence Count  : " + countSentences);
        System.out.println("Avg. Word Count : " + avgSentences / countSentences);

        System.out.println();
        System.out.println();

        TreeMap<String, Integer> sortedMap = sortMapByValue((HashMap<String, Integer>) prop);
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            System.out.println(entry.getKey() + " (" + entry.getValue() + ")");
        }
    }

}

class MyRunnable implements Runnable {
    private final String sentence;

    public MyRunnable(String sentence) {
        this.sentence = sentence;
    }

    @Override
    public void run() {
        String[] strArr = sentence.split(" ");

        for (String str : strArr) {
            if (Vaka.prop.get(str) == null) {
                Vaka.prop.put(str, 1);
            } else {
                Vaka.prop.put(str, Vaka.prop.get(str) + 1);
            }
        }
    }
}
class ValueComparator implements Comparator<String>{

    HashMap<String, Integer> map = new HashMap<>();

    public ValueComparator(HashMap<String, Integer> map){
        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {
        if(map.get(s1) >= map.get(s2)){
            return -1;
        }else{
            return 1;
        }
    }
}
