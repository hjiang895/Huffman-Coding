# Problem Set 8: Huffman coding

## Due Monday, April 1 @ 11:59pm

---

## Problem Set Overview

This is a pair problem set. Find one partner to work with. If you do not have a partner, email me, and I will try match you up with someone else who is also looking for a partner. Please include the full names of both team members in the comments at the top of your files.

### Goals

In this problem set, you will team up with one other classmate to design and develop a program called `**Huff.java**` which will perform Huffman compression of a text file. If you write your code correctly, you will be able to use the provided `Puff.jar` program to inflate (decompress) any file that has been compressed by your `Huff` program. 

### What you will see in this repository

In addition to a hidden `.gitignore` file, this folder contains:

```
Huff.jar        README.md       src/
Puff.jar        samples/
```

The `src` directory a number of helper files that you will need to use. `FileIO.java` is an interface (ADT) for reading input and writing output, and `FileIOC.java` is an implementation of that interface. You will use this class for reading in the file you want to compress and for writing out the compressed file. I demonstrate how to use this class in the `Huff.java` file provided.

In addition, the `src` directory contains well commented starter code for `Huff.java` and `HuffTree.java`. Both files have a lot of code already written for you. The instructions, below, are paraphrased in the comments for these two files.

The `samples` directory contains a few test files that you can use to test your code. I recommend starting with `mississippi.txt` which is just the word "MISSISSIPPI".

The file `Huff.jar` is a Java archive containing a reference implementation of the *compression* half of the codec. The file `Puff.jar` is a Java archive containing a reference implementation of the *decompression* half of the codec. These `.jar` files contain only compiled Java classes. You will not be able to view the actual code without sneaky efforts.

You can use these `.jar` files to test whether your code is doing what it should be doing. If you can compress a file with *your* `Huff.java` implementation, and decompress it with the included `Puff.jar` file, then your code works. You can also inspect the output of `Huff.jar` and compare it to the output of your code for `Huff.java`.

Without writing any code, you can try out the jar files from a command line as follows.

```
> cp samples/lincoln.txt .              # make a copy of lincoln.txt in your current directory
> java -jar Huff.jar lincoln.txt        # this produces file lincoln.zip
> rm lincoln.txt                        # this deletes the text file you just zipped
> java -jar Puff.jar lincoln.zip        # this produces a new file lincoln.txt
> diff lincoln.txt samples/lincoln.txt  # compare the two files
>                                       # if there's no output from diff, success!
```
Once you have your version of `Huff.java` working properly, you will be able to replace the second command above with 

```
> java Huff lincoln.txt
```

and get exactly the same results.

### Four tasks

There four tasks involved in this problem set:

1. Reading in a text file and keeping track of how many times you see each character in order to create a frequency table, which you will store in a `TreeMap`.

2. Building a Huffman Tree using those frequencies.

3. Extracting the Huffman codes from the Huffman Tree for each character in the text, which you will store in a `TreeMap`.

4. Writing this information, along with a Huffman encoded version of the original text, to a binary file, which will be your compressed file. 


## Task 1: Creating a frequency table: `readInFile()` method in `Huff.java`
I've given you a `TreeMap` in `Huff.java` called `charCounts` that will serve as your frequency table. It will map a character (stored as an `Integer`, which is the ASCII code for that character) to its frequency (also stored as an `Integer`).

Complete the implementation of `readInFile()` in `Huff.java`. For each character you read in, if that character aready exists as a key in the `TreeMap`, add 1 to its value (i.e., its current frequency total) in the `TreeMap`. Otherwise, add it as a key to the `TreeMap` with value `1`.


## Task 2: Building the Huffman tree
Next you are going to build a Huffman tree that you will be able to traverse to generate the Huffman code (i.e., the sequence of 1s and 0s) for each character, just as shown in class. Some of the code for this step will go in `Huff.java` and some of it will go in `HuffTree.java`.

### `mergeTrees()` in `HuffTree.java`
First implement `mergeTrees()` in `HuffTree.java`. This takes two HuffTree objects as arguments and merges those two trees into the calling `HuffTree` object. Make the **right child** of the calling `HuffTree` point at the `top` node of the **first argument** `HuffTree`. Make the **left child** of the calling `HuffTree` point at the `top` node of the **second argument** `HuffTree`. Make the `weight` variable of the calling `HuffTree` object be equal to the sum of the weights of the two argument `HuffTree`s. The `character` variable will remain unchanged (i.e., -1).

### `buildHuffmanTree()` in `Huff.java`
Next implement the `buildHuffmanTree()` method in `Huff.java`. I've created a `PriorityQueue` of `HuffTree` objects for you. (*You SHOULD NOT write your own priority queue implementation. You can read about Java's `PriorityQueue` class [here](https://docs.oracle.com/javase/8/docs/api/java/util/PriorityQueue.html)*) You will need to populate the `PriorityQueue` with one `HuffTree` per character, as follows.

For each character key in your `TreeMap`, create a `HuffTree` instance. Initially, the `top` of each `HuffTree` will be a `Node` that has null pointers for its right child and left child, and has the `character` variable set to the character and the `weight` variable set to the frequency of that character. Add each new `HuffTree` you create to the `PriorityQueue` using the `add()` method.

You will now have a `PriorityQueue` with one `HuffTree` for each character. While there is more than one `HuffTree` in the `PriorityQueue`, follow this procedure to iteratively merge `HuffTree` objects into a single HuffmanTree: (1) create a new empty `HuffTree` object, called `t3`; (2) use the `poll()` method in `PriorityQueue` to get the two `HuffTree`s with the smallest weights, `t1` and `t2`. Call `mergeTrees()` on `t3` with the first argument as `t1` and the second argument as `t2`. Add `t3` to the `PriorityQueue` using `add()`. 


## Task 3: Extracting the Huffman codes
After merging all these `HuffTree` objects, the `PriorityQueue` now contains exactly one element: the full Huffman tree for the input text. Remove the remaning `HuffTree` from the priority queue, and save it to the `finalTree` instance variable. 

###  `getCodes()` in `HuffTree.java`
In `HuffTree.java`, complete the implementation of `getCodes()`. This method reads off the path to each leaf (character) in the calling `HuffTree` and saves out the character-path pair to its `codes` `TreeMap` instance variable. Here is some pseudocode you might find useful.

```
public String getCodes(Node n, String path) {
  if n is null
    return s

  if the node's leftchild and rightchild are both null
    you are at a leaf!
    enter character of n and its path as a key-value pair in the codes TreeMap
    return s
  
  if the left child is not null
    create a new String by appending "0" to path  
    call the method with the left child and the new string as arguments
  
  if the right child is not null
    create a new string by appending "1" to path  
    call the method with the left child and the new string as arguments
}  
```

### `extractCodes()` in `Huff.java`
In the `extractCode()` method in `Huff.java`, call the `getCodes()` method on `finalTree`, and then set `huffCodes` in `Huff.java` to equal the `codes` variable of `finalTree`.


## Task 4: Writing to a binary file: `writeOutFile()` in `Huff.java`
You will use the `BinaryOut` class, an instance of which is created by the `FileIOC` class in `Huff.java`, to print out to the compressed binary file. Complete the implementation of the `writeOuFile()` method in `Huff.java`, as follows.

1. Open the binary output file. (Code for this is included in `Huff.java`.)

2. Write out the signature two-byte code (`0x0bc0`), which identifies the file as our special zip file. This and all of the following steps that involve writing out to the binary file will use the overloaded `write()` method  in the `BinaryOut` class, as shown in the `Huff.java` code I have provided.

3. Write out, as an integer (32 bits, i.e., 4 bytes), the number of chars (keys) in the frequency table `TreeMap`.

4. Next write out the symbol frequency information. For each key in the symbol table, write the key (i.e., the character) using one byte (32 bits) and write its integer frequency using 4 bytes. Remember, a `char` type is really just a value that corresponds to the decimal ASCII code for that character. To write out a `char` using only one byte, you can use the version of `write()` in `BinaryOut` that takes two arguments: a char and the number of bits in the char that you want to print out.

5. Reopen the input file, and process it character by character.

6. For each character in the input file, look up its bit pattern in the frequency table and write it out to the binary file. You have two options for doing this using the `write()` method in `BinaryOut`:

* You can convert the string of 0s and 1s to an int using `Integer.parseInt()`, with the first argument being the String of 0s and 1s and the second argument being the int radix, 2. This will create an int that corresponds to the value expressed by string of 0s and 1s in binary. You can then call the `write()` method of `BinaryOut` with two arguments: the int you just created and the number of bits you want to print, which will be length of the original string.

```java
    String t = "101";
    int i = Integer.parseInt(t, 2);
    bo.write(i, t.length());
```

* You can proceed through the string of 0s and 1s, print out each as a boolean: false for 0 and true for 1.
```java
    // to write a zero, write false
    bo.write(false);
```

7. Close the file.

---

## Helpful Hints

#### Working with binary files: hexdump

The Huff and Puff programs write and read binary files, so it will be helpful to have a tool that lets you view the contents of binary files so you can make sure you are printing out the correct bits and bytes.

On a Mac or Unix system, there is a command line utility called `hexdump` that you can use to take any file and print out its hexadecimal representation. On Windows 10 in the PowerShell, you can use the `Format-Hex` command. Suppose you used the `Puff.jar` to compress the sample file `lincoln.txt`. From a command line you can type:

`hexdump lincoln.zip` (or `Format-Hex lincoln.zip` in Windows 10)

and the hexadecimal version of the file will appear, as demonstrated in class.

If you're having trouble wrapping your head around all the different bases (decimal, binary, and hex), check out this very useful online conversion tool:

https://www.rapidtables.com/convert/number/

And don't forget to find a handy ASCII table for when you are reading your hexdump and you want to make sure you've written out the right characters.

http://www.asciitable.com


#### Bits of variable length

Huffman codes are *variable length* codes. For example, the letter `'A'` may be represented by the 3-bit string `101` while the letter `'B'` may be represented by the 2-bit string `11`. Remember: you can't just take the string of 0s and 1s for a code, convert it to an int and print it out. If you do, you will be unnecessarily adding lots of extra zeros at the front in order to make it 32 bits. You are trying to write the fewest bits possible to your binary file. 

Some options for printing out only the bits you need are discussed above in item 6 of Task 4, above.

#### The char data type and ASCII representations

Both the Huff and Puff programs will require a table data structure (a `TreeMap`) that allows them to look up information about symbols (i.e., characters) that occur in the input text. As you know, variables of the `char` type are represented by small integers. For example, the ASCII-assigned integer representation of the letter 'A' is 65. Find an ASCII table (like [this one](http://www.asciitable.com)) that includes both decimal and hex values for each character so that you can make sure you're doing everything right. 

Java has a 16-bit (2-byte) char data type. When you write out to the binary file, you are required to use only 8 bits in your frequency table. I discuss a way to write out a 16-bit char to 8 bits in the binary file above, in part 6 of Task 3. 

---

## Important notes on grading

1. The files **must be in the `src` directory**. Do not move files around, or things will go very wrong. You will lose points for moving files out of the `src` directory.

2. Your code must compile. If it does not compile, you will get a 0. Get started early so that you do not run into compilation errors in the late evening the day it is due.

3. Comment your code and indent propertly. This will be worth 10% of your grade on this problem set (i.e., 1 point).



