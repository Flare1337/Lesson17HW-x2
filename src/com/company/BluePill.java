package com.company;

import java.io.*;
import java.util.ArrayList;

public class BluePill {

    public static void main(String[] args) {
        BluePill bluePill = new BluePill();

        String message = "Hello, world!";
        Contact Annie = new Contact("Annie", "Smith", "+3512123123", 1999);
        Contact John = new Contact("John", "Reyes", "+3502123111", 1990);
        Contact Michael = new Contact("Michael", "Cox", "+3512883125", 1985);
        Contact Christie = new Contact("Christie", "Bell", "+3992123127", 2005);
        Contact Andrew = new Contact("Andrew", "Smith", "+3512993123", 1999);
        Contact Rachael = new Contact("Rachel", "Rivera", "+3512684125", 1986);
        Contact Tim = new Contact("Tim", "Henson", "+3531867234", 1995);
        Contact Steve = new Contact("George", "Orwell", "+3565423123", 1984);
        Contact Daniel = new Contact("Daniel", "Miller", "+3592368123", 1980);
        Contact Keith = new Contact("Keith", "Brann", "+3512152350", 1992);

        Contact[] contacts = new Contact[10];

        contacts[0] = Annie;
        contacts[1] = John;
        contacts[2] = Michael;
        contacts[3] = Christie;
        contacts[4] = Andrew;
        contacts[5] = Rachael;
        contacts[6] = Tim;
        contacts[7] = Steve;
        contacts[8] = Daniel;
        contacts[9] = Keith;

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("Java");
        stringList.add("is");
        stringList.add("an");
        stringList.add("old");
        stringList.add("language");

        ArrayList<Contact> contactList = new ArrayList<>();
        contactList.add(Annie);
        contactList.add(John);
        contactList.add(Michael);
        contactList.add(Christie);
        contactList.add(Andrew);

        try {
            bluePill.writeDataToFile(message, MyFile.FILE_FOR_TEXT);
            bluePill.putThousandNumbersToFile(MyFile.FILE_FOR_NUMBERS);
            bluePill.writeContactsToFile(contacts, MyFile.FILE_FOR_CONTACTS_ONLY);

            System.out.println("1, 3, 4 и 5 задания домашки №21");
            bluePill.writeElementsByDataOutputStream(MyFile.BINARY_FILE);
            bluePill.writeAndReadStringsByDataStream(stringList, MyFile.FILE_FOR_TEXT);
            bluePill.writeAndReadContactsByDataStream(contactList, MyFile.BINARY_FILE);
            bluePill.writeAndReadContactsWithSerialization(contactList, MyFile.BINARY_FILE);

            System.out.println("домашка №23");
            bluePill.copyFileUsingInputOutput(args[0], args[1]);
            bluePill.copyFileUsingInputOutputButLocalBuffer(args[0], args[1]);
            bluePill.copyFileUsingBufferedStream(args[0], args[1]);
            bluePill.copyFileUsingBufferedStreamWithBigBuffer(args[0], args[1]);


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeContactsToFile(Contact[] contacts, String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(fileName))) {
            var count = 0;
            for (Contact contact : contacts) {
                writer.write(contact.getFirstName() + " | " + contact.getLastName() + " | " +
                        contact.getPhoneNumber() + " | " + contact.getBirthday() + "\n");
                ++count;
                if (count == 10) {
                    break;
                }
            }
        }
    }

    public void writeDataToFile(String message, String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(fileName))) {
            writer.println(message);
        }
    }

    public void putThousandNumbersToFile(String fileName) throws IOException {
        int number = 0;
        final var MAXIMUM = 1000;
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(fileName))) {
            for (int count = 0; count < MAXIMUM; count++) {
                number = (int) (Math.random() * (1150 + 1)) - 500;
                if (count == (MAXIMUM - 1)) {
                    writer.write(number);
                }
                    writer.write(number);
                    writer.write(", ");
            }
        }
    }

    public void writeElementsByDataOutputStream(String fineName) throws IOException {
        try (DataOutputStream dataOutStream = new DataOutputStream(new FileOutputStream(fineName))) {
            dataOutStream.write(123);
            dataOutStream.writeBoolean(false);
            dataOutStream.writeUTF(fineName);
            dataOutStream.writeChar('s');
        }
    }

    public void writeAndReadStringsByDataStream(ArrayList<String> strings, String fineName) throws IOException {
        try (DataInputStream dataInStream = new DataInputStream(new FileInputStream(fineName));
             DataOutputStream dataOutStream = new DataOutputStream(new FileOutputStream(fineName))) {
            for (String s : strings) {
                dataOutStream.writeUTF(s);
            }
            while (dataInStream.available() > 0) {
                System.out.println(dataInStream.readUTF());
            }
        }
    }

    public ArrayList<Contact> writeAndReadContactsByDataStream(ArrayList<Contact> contacts, String fineName) throws IOException {
        ArrayList<Contact> resultedContacts = new ArrayList<>();
        try (DataInputStream dataInStream = new DataInputStream(new FileInputStream(fineName));
             DataOutputStream dataOutStream = new DataOutputStream(new FileOutputStream(fineName))) {
            for (Contact contact : contacts) {
                dataOutStream.writeUTF(contact.getFirstName() + " | " + contact.getLastName() + " | " +
                                            contact.getPhoneNumber() + " | " + contact.getBirthday());
            }
            while (dataInStream.available() > 0) {
                String[] contact = dataInStream.readUTF().split(" \\| ");
                Contact recreatedContact = new Contact(contact[0], contact[1], contact[2], Integer.parseInt(contact[3]));
                resultedContacts.add(recreatedContact);
            }
        }
        return resultedContacts;
    }

    public ArrayList<Contact> writeAndReadContactsWithSerialization(ArrayList<Contact> contacts, String fineName)
            throws IOException, ClassNotFoundException {
        ArrayList<Contact> resultedContacts = new ArrayList<>();
        try (ObjectInputStream dataInStream = new ObjectInputStream(new FileInputStream(fineName));
             ObjectOutputStream dataOutStream = new ObjectOutputStream(new FileOutputStream(fineName))) {
            for (Contact contact : contacts) {
                dataOutStream.writeObject(contact);
            }
            while (dataInStream.available() > 0) {
                Contact contact = (Contact) dataInStream.readObject();
                resultedContacts.add(contact);
            }
        }
        return resultedContacts;
    }

    public void copyFileUsingInputOutput(String from, String to) throws IOException {
        try (InputStreamReader dataInStream = new InputStreamReader(new FileInputStream(from));
             OutputStreamWriter dataOutStream = new OutputStreamWriter(new FileOutputStream(to))) {
            while (dataInStream.ready()) {
                dataOutStream.write(dataInStream.read());
            }
        }
    }

    public void copyFileUsingInputOutputButLocalBuffer(String from, String to) throws IOException {
        try (FileInputStream dataInStream = new FileInputStream(from);
             FileOutputStream dataOutStream = new FileOutputStream(to)) {

            while (dataInStream.available() > 0) {
                byte[] bytesBuffer = new byte[4096];
                for (int index = 1; index <= bytesBuffer.length && dataInStream.available() > 0; index++) {
                    bytesBuffer[index] = (byte) dataInStream.read();
                }
                dataOutStream.write(bytesBuffer);
                bytesBuffer = new byte[0];
            }
        }
    }

    public void copyFileUsingBufferedStream(String from, String to) throws IOException {
        try (BufferedInputStream dataInStream = new BufferedInputStream(new FileInputStream(from));
             BufferedOutputStream dataOutStream = new BufferedOutputStream(new FileOutputStream(to))) {
            while (dataInStream.available() > 0) {
                dataOutStream.write(dataInStream.read());
            }
        }
    }

    public void copyFileUsingBufferedStreamWithBigBuffer(String from, String to) throws IOException {
        try (BufferedInputStream dataInStream = new BufferedInputStream(new FileInputStream(from));
             BufferedOutputStream dataOutStream = new BufferedOutputStream(new FileOutputStream(to))) {
            while (dataInStream.available() > 0) {
                    byte[] bytesBuffer = new byte[4096];
                    for (int index = 1; dataInStream.available() > 0 && index <= bytesBuffer.length; index++) {
                        bytesBuffer[index] = (byte) dataInStream.read();
                    }
                    dataOutStream.write(bytesBuffer);
                    bytesBuffer = new byte[0];
            }
        }
    }
}
