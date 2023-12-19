package com.example.tp_chat_multithread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class multithreadchatserver_v2 extends Thread{
    private List<Conversation> conversationList=new ArrayList<>();

    String nomclient;
    InputStream is ;
    InputStreamReader isr;

    BufferedReader br;
    PrintWriter pw;

    public static void main( String[] args )
    {
        new multithreadchatserver_v2().start();

    }
    @Override
    public void run(){
        System.out.println("the server is started using port <1234>");
        try {
            ServerSocket serversocket = new ServerSocket(1234);
            while (true){
                Socket socket = serversocket.accept();
                System.out.println("serveur est connecter");
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                nomclient=br.readLine();
                System.out.println("nomclient envoyer : "+nomclient);
                //nomclient="mounsif";
                Conversation conversation = new Conversation(socket,nomclient);
                conversationList.add(conversation);
                conversation.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    class Conversation extends Thread{
        private Socket socket;
        private String nomclient;
        public Conversation(Socket socket,String nomclient){
            this.socket=socket;
            this.nomclient=nomclient;
        }
        @Override
        public void run(){
            try {
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

                OutputStream os = socket.getOutputStream();
                pw = new PrintWriter(os, true);
                /*pw.println("response");
                pw.flush(); dans le cas de false*/
                String name = socket.getRemoteSocketAddress().toString();
                System.out.println("new client conn =>  name : " + nomclient + ":"+ name);
                pw.println("welcome your name is : " + nomclient);
                String request;
                while ((request = br.readLine()) != null) {
                    //String request=br.readLine();
                    System.out.println("new request => ip =" + name + ",name :"+nomclient+",request=" + request);
                    List<String> clientsto = new ArrayList<>();
                    String message;
                    if (request.contains("=>")) {
                        String[] items = request.split("=>");
                        String clients = items[0];
                         message = items[1];
                        if (clients.contains(",")) {
                            String[] clientid = clients.split(",");
                            for (String id : clientid) {
                                clientsto.add(id);
                            }
                            //String client=clientid[0];
                        } else {
                            clientsto.add(clients);
                        }
                    }
                    else {
                        clientsto=conversationList.stream().map(c->c.nomclient).collect(Collectors.toList());
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
                    if(conversation!=from && clients.contains(conversation.nomclient)){
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

