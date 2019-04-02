import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public class Huff {

  static boolean DEBUG=true;

  ////////////////////////
  // INSTANCE VARIABLES //
  ////////////////////////
  // charCounts stores the count of each character in the file.
  // huffCodes stores the eventual Huffman code for each character.
  // We store the characters as integers, where the int is the ASCII code.
  TreeMap<Integer, Integer> charCounts = new TreeMap<Integer, Integer>();
  TreeMap<Integer, String> huffCodes = new TreeMap<Integer, String>();

  // finalTree stores the final Huffman tree that you create in TASK 2.
  HuffTree finalTree;

  FileIOC fioc = new FileIOC();


  /////////////
  // METHODS //
  /////////////

  // METHOD FOR TASK 1
  // This method will read in a file and counts the number
  // of times each char apears in that file.
  // It stores this information in the instance variable,
  // TreeMap charCounts, above.
  public void readInFile(String infile) throws IOException {
    FileReader fr = fioc.openInputFile(infile);

    // This code below goes through the file character by character
    // so you can count them. Note that we are reading in each
    // character as an int.
    int c;
    while ((c = fr.read()) != -1) {

      // Here's an example of something to do: print out each character.
      System.out.println((char) c);


      ////////////////////////////
      // YOUR CODE HERE: TASK 1 //
      ////////////////////////////
      if(charCounts.containsKey(c)){
        charCounts.replace(c, charCounts.get(c) + 1);
      }
      //if character c is already in treemap, add one to value of that character
      else{
        charCounts.put(c, 1);
      }
      //else add c as a new key with value of 1

      // This is where you count how many times each character, c, appears.
      // You store the frequency of each character in the TreeMap charCounts.
      // The first time you see c, add it as a key to charCounts with value 1.
      // Each subsequent time you see c, add one to its value in the map.
      // You'll be using the get(), put(), and containsKey() methods in TreeMap.
    }

    // You have to close the file, just the way you would in Python.
    fr.close();

    // This code will print out your TreeMap.
    // Remember that space and newline count as characters!!!
    for (int k : charCounts.keySet()) {
      System.out.println((char)k + " " + k + " "+ charCounts.get(k));
    }


  }

  // METHOD FOR TASK 2
  // This method takes the information in charCounts and builds
  // the final Huffman tree, finalTree.

  public void buildHuffmanTree() {

    // Create a PriorityQueue of HuffTrees
    PriorityQueue<HuffTree> pq = new PriorityQueue<HuffTree>();

    ////////////////////////////
    // YOUR CODE HERE: TASK 2 //
    ////////////////////////////

    // For each key, value pair in the charCounts TreeMap, create a HuffTree
    // whose character field is the key and whose weight is the value.
    // Insert that HuffTree into the PriorityQueue.
    // Go look at the HuffTree code to learn how to create a HuffTree.
    // There are multiple constructors for HuffTree and its Node.

    for(int k : charCounts.keySet()){
      HuffTree huffie = new HuffTree(k, charCounts.get(k));
      pq.add(huffie);
    }
    //for every key-value pair in charCounts, we create a HuffTree and add it to
    //the priority queue
    while(pq.size() > 1){
      HuffTree t3 = new HuffTree();
      t3.mergeTrees(pq.poll(), pq.poll());
      pq.add(t3);
    }
    //we traverse the priority queue and create a new HuffTree which merges the
    //two smallest HuffTrees, then add them back to the PriorityQueue
    finalTree = pq.poll();
    //we assign instance vairable finalTree to the last variable of the last
    //variable of the PriorityQueue

    // You now have a `PriorityQueue` with one `HuffTree` for each character.
    // While there is more than one `HuffTree` in the `PriorityQueue`,
    // `poll()` off the two `HuffTree`s with the smallest weights.
    // Let's call the first one you poll **t1** and the second **t2**.
    // Finish writing the mergeTrees() method in HuffTree.java, and then use it
    // to construct a new `HuffTree` t3 that has t1 as its left child, and t2
    // as right child; and with t3.weight = t1.weight() + t2.weight().
    // Insert the new `HuffTree` **t** into the priority queue.
    // Keep doing this until the PQ has only one HuffTree.


    // LAST but not least: set the finalTree instance variable to pq.poll()

  }


  // METHOD FOR TASK 3
  // THe work for this will be done in HuffTree.java.
  // In HuffTree.java you must implement getCodes() as desribed in HuffTree.java.
  // If that is done correctly, this method will work without changing anything.
  public void extractCodes() {
    finalTree.getCodes(finalTree.top, "");
    huffCodes = finalTree.codes;
    System.out.println(huffCodes);
  }


  // METHOD FOR TASK 4
  // This is the method that writes out the compressed file.
  public void writeOutFile(String infile)  throws IOException {

    // FileIOC uses the BinaryOut class, which lets you write out bits one at a time.
    // FileIOC will automatically open a file with the same name as your input file
    // but it will replace .txt with .zip.
    BinaryOut bo = fioc.openBinaryOutputFile();

    // The BinaryOut class has a write() method that can print out all the
    // primitive data types to binary. I give you code and hints below.

    // This line prints out the "signature" two bytes that must begin our zip file.
    // A short is a two-byte datatype, so use a short.
    short s = 0x0bc0;
    bo.write(s);


    ////////////////////////////
    // YOUR CODE HERE: TASK 4 //
    ////////////////////////////


    // Next, write out, as an integer (32 bits, i.e., 4 bytes),
    // the number of keys in the frequency table `TreeMap`, charCounts. This
    // tells the program reading the file how many different characters we counted.
    // Again, you will just use bo.write()

    int size = charCounts.size();
    bo.write(size);

    // Next write out the character frequency information for each character.
    // For each key in the charCounts TreeMap, write the key itself using
    // one byte (8 bits), followed by its integer frequency using 4 bytes (32 bits).
    // To write out an Integer using only one byte, you can use the version of
    // `write()` in `BinaryOut` that takes two arguments: a char and the number
    // of bits in the char that you want to print out, e.g., write(myint, 8).

    // This code below goes through the file character by character
    // so you can count them. Note that we are reading in each
    // character as an int.

    for(int k : charCounts.keySet()){
      bo.write(k, 8);
      bo.write(charCounts.get(k), 32);
    }
    //iterates through the treemap charCounts and translate sthe character and frequency
    //as bits

    // Next, reopen the input file, and process it character by character.
    // You should re-use the code provided in readInFile(), above.
    // For each character in the input file, look up its bit pattern in the
    // huffCodes TreeMap and write it out to the binary file.
    // See the README for two different ways of doing this!

    FileReader fr = fioc.openInputFile(infile);

    int c;
    while ((c = fr.read()) != -1) {
      String t = huffCodes.get(c);
      int i = Integer.parseInt(t, 2);
      bo.write(i, t.length());
    }
    // for each character in the text, finds corresponding huffCode and converts it to binary int
    // writes the int huffCode using t.length() number of bits in the binary output

    // Finally, files have to be written in bytes not bits. The BinaryOut
    // write() method will bunch your bits together into bytes for you, but
    // when you write out your compressed file, you might end up with some
    // number of bits that isn't divisible by 8. Thus, at the end of your file,
    // you need to "flush" the output, as shown below. flush() will add 0s at
    // the end of your file until you complete a byte.
    bo.flush();

    // Do this to close the output stream.
    bo.close();

  }




  // MAIN METHOD
  public static void main (String[] args) throws IOException {


    Huff myhuff = new Huff();

    // Uncomment each of the TASKs below as you complete
    // their implementations, above.

    // TASK 1: Read in the file and count frequency of each character
    myhuff.readInFile(args[0]);


    // TASK 2: Build the Huffman tree
    myhuff.buildHuffmanTree();


    // TASK 3: Extract Huffman codes
    myhuff.extractCodes();


    // TASK 4: Write out the compressed file
    myhuff.writeOutFile(args[0]);

  }

}
