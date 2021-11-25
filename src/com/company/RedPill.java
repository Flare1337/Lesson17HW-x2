package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class RedPill {
    int counter = 0;

    public static void main(String[] args) {
        try {
            RedPill redPill = new RedPill();
            redPill.readNumbersFromFileAndFindAverage(MyFile.FILE_FOR_NUMBERS);
            redPill.displaySortedContacts(redPill.sortContacts(redPill.readContactsFromFile(MyFile.FILE_FOR_CONTACTS_ONLY)));

            System.out.println("2 и 6 задание домашки №21");
            redPill.readElementsByDataInputStream(MyFile.BINARY_FILE);
            redPill.findFilesCount(MyFile.PATH_WITH_FILES);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int readNumbersFromFileAndFindAverage(String fileName) throws IOException {
        List<Integer> integers = new ArrayList<>();
        List<Double> doubles = new ArrayList<>();
        var integer = 0;
        var doub = 0.0;
        var count = 0;
        try (Scanner scanner = new Scanner(new FileInputStream(fileName))) {
            while (scanner.hasNextInt() || scanner.hasNextDouble()) {
                ++count;
                if (count > 99) {
                    break;
                }
                if ((doub = scanner.nextDouble()) > 0) {
                    doubles.add(doub);
                }
                else if ((integer = scanner.nextInt()) > 0) {
                    integers.add(integer);
                }
            }
        }
        var sum = 0;
        var result = 0;
        for (Integer number : integers) {
            sum += number;
        }
        for (Double number : doubles) {
            sum += number;
        }
        result = sum / (integers.size() + doubles.size());
        return result;
    }

    public ArrayList<Contact> readContactsFromFile(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String line = "";
            ArrayList<Contact> contacts = new ArrayList<>();
           while ((line = reader.readLine()) != null) {
                String[] contactData = line.split(" \\| ");
                String firstName = contactData[0];
                String lastName = contactData[1];
                String phone = contactData[2];
                int birthday = Integer.parseInt(contactData[3]);
                contacts.add(new Contact(firstName, lastName, phone, birthday));
           }
           return contacts;
        }
    }

    public ArrayList<Contact> sortContacts(ArrayList<Contact> contacts) {
        for (int outer = contacts.size() - 1; outer > 1; outer--) {
            for (int inner = 0; inner < outer; inner++) {
                if (contacts.get(inner).getBirthday() < contacts.get(inner + 1).getBirthday()) {
                    Collections.swap(contacts, inner, inner + 1);
                }
            }
        }
        return contacts;
    }

    public void displaySortedContacts(ArrayList<Contact> contacts) {
        var counter = 0;
        for (Contact contact : contacts) {
            counter++;
            System.out.println("Имя, фамилия, телефон и день рождения: " + contact.getFirstName() + " "
                    + contact.getLastName() + " " + contact.getPhoneNumber() + " " + contact.getBirthday());
            if (counter == 5) {
                break;
            }
        }
    }

    public void readElementsByDataInputStream(String fineName) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(fineName))) {
            int first = dataInputStream.readInt();
            boolean second = dataInputStream.readBoolean();
            String third = dataInputStream.readUTF();
            char fourth = dataInputStream.readChar();
            System.out.println("" + first + second + third + fourth);
        }
    }

    public int findFilesCount(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files != null) {
            for (File value : files) {
                if (!value.isDirectory())
                    counter++;
                if (value.isDirectory())
                    findFilesCount(value.getPath());
            }
        }
        return counter;
    }
}
