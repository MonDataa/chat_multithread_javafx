package com.example.tp_chat_multithread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class multithreadchatserver_v3 extends Thread{
    private List<Conversation> conversationList=new ArrayList<>();
    String nomclient;
    public static void main( String[] args )
    {
        new multithreadchatserver_v3().start();

    }
    @Override
    public void run(){
        System.out.println("the server is started using port <1234>");
        try {
            ServerSocket serversocket = new ServerSocket(1234);
            while (true){
                Socket socket = serversocket.accept();
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);

                nomclient=br.readLine();
                System.out.println("nomclient envoyer : "+nomclient);
                Conversation conversation = new Conversation(socket,nomclient);
                conversationList.add(conversation);
                //pw.println(conversation.nom);
                conversation.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    class Conversation extends Thread{
        private Socket socket;
        private String nom;
        public Conversation(Socket socket,String nom){
            this.socket=socket;
            this.nom=nom;
        }
        @Override
        public void run(){
            try {
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                /*pw.println("response");
                pw.flush(); dans le cas de false*/
                String ip = socket.getRemoteSocketAddress().toString();
                System.out.println("new client conn => " + nom + " ip : " + ip);
                pw.println("welcome your name is : " + nom);
                String request;
                while ((request = br.readLine()) != null) {
                    //String request=br.readLine();
                    System.out.println("new request => ip =" + ip + "request=" + request);
                    List<String> clientsto = new ArrayList<>();
                    String message;
                    if (request.contains("=>")) {
                        String[] items = request.split("=>");
                        String clients = items[0];
                         message = items[1];
                        if (clients.contains(",")) {
                            String[] clientname = clients.split(",");
                            for (String n : clientname) {
                                clientsto.add(n);
                            }
                            //String client=clientid[0];
                        } else {
                            clientsto.add(clients);
                        }
                    }
                    else {
                        clientsto=conversationList.stream().map(c->c.nom).collect(Collectors.toList());
                        /*String[] data = request.substring(1, request.length() - 1).split(", ");
                        String name_envoye = data[0];
                        message = data[1];
                        // Affichage des données reçues
                        System.out.println("affichage du Nom cote serveur: " + name_envoye + ", et Message cote serveur: " + message);
                        */message=request;
                    }
                    broadcastMessage(message,this);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public void broadcastMessage(String message,Conversation from) throws IOException {
            List<Object> message_name=new ArrayList<Object>();
            Socket socket;
            OutputStream outputStream;
            PrintWriter printWriter;
            try {
                message_name.add(from.nom);
                message_name.add(message);
                for(Conversation conversation:conversationList) {
                        socket = conversation.socket;
                        outputStream = socket.getOutputStream();
                        printWriter = new PrintWriter(outputStream, true);
                        printWriter.println(message);
                System.out.println("affiche message (broadcastMessage) : " + message_name.get(1));
                System.out.println("affiche this = from (broadcastMessage) : " + message_name.get(0));
            }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

