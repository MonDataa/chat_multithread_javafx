package com.example.tp_chat_multithread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class multithreadchatserver_v22 extends Thread{
    private List<Conversation> conversationList=new ArrayList<>();
    String nomclient;
    public static void main( String[] args )
    {
        new multithreadchatserver_v22().start();

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
                        message=request;
                    }
                    broadcastMessage(message,this,clientsto);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public void broadcastMessage(String message,Conversation from,List<String> clients) throws IOException {

            try {
                for(Conversation conversation:conversationList) {
                    if(conversation!=from && clients.contains(conversation.nom)){
                        Socket socket = conversation.socket;
                        OutputStream outputStream = socket.getOutputStream();
                        PrintWriter printWriter = new PrintWriter(outputStream, true);
                        printWriter.println(message);
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

