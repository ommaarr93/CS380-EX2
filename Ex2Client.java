// Omar Rodriguez
// CS 380
// Professor Nima Davarpanah

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.CRC32;

public final class Ex2Client {

  private static Socket soc;
  private static OutputStream out ;
  private static InputStream in;
  private static DataInputStream dis;


  public static void main(String[] args) throws IOException {
    try {
      soc = new Socket("codebank.xyz", 38102);
      out = soc.getOutputStream();
      in = soc.getInputStream();
      dis = new DataInputStream(in);
    }
    catch(UnknownHostException e) {
      System.out.println("Unknown Host");
      System.exit(0);
    }
    try {
      readHalfBytes();
      crc();
      writeReturnMessage();
      out.write(returnData);

      if(dis.read() == 1) {
        System.out.print("\n\nResponse good");
      }
      else {
        System.out.print("\n\nResponse bad");
      }
    }
    catch(IOException e){

    }
    in.close();
    soc.close();
    dis.close();
    out.close();
    System.out.println("\n\nDisconnected from server.");
  }

  private static byte [] returnData;
  private static int crCode;

  private static void writeReturnMessage() {
		returnData = new byte[4];
		int temp = crCode;
		int j = 3;

		for (int i = 0; i < 4; i++) {
			temp = crCode >> (8*j);
			temp = temp & (0x00FF);
			returnData[i] = (byte)temp;
			//System.out.println(temp);
			j--;
		}
		System.out.print("\n\nGenerated CRC32: ");
		System.out.print(javax.xml.bind.DatatypeConverter.printHexBinary(returnData) );
	}

  private static void crc() {
		CRC32 cr = new CRC32();
		cr.update(data);
		crCode= (int)cr.getValue();
	}

  public static void readHalfBytes() throws IOException {
		data = new byte[100];

		for(int i = 0; i < 100; i++ )
		{
			int fullByte = dis.readByte();

			fullByte = fullByte << 4;
			int otherByte = dis.readByte();
			fullByte = (fullByte ^ otherByte);
			data[i] = (byte)fullByte;
		}
		System.out.println("Recieved bytes: ");
		printData();
	}

  private static String str;
  private static byte [] data;

  public static void printData() {
    int j = 0 ;
		String str = javax.xml.bind.DatatypeConverter.printHexBinary(data);
		for (int i  =  0; i < str.length(); i++) {
			if (i >= (20 * (j+1))) {
				System.out.println("");
				j++;
			}

			System.out.print(str.charAt(i) + "");
		}
	}
}
