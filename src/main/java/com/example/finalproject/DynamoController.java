package com.example.finalproject;

/*public class UserModel {
    private String id;
    private String name;
    private Integer age;
    private Boolean isProfessor;

    public void UserModel(String name, Integer age, Boolean isProfessor) {
        this.name = name;
        this.age = age;
        this.isProfessor = isProfessor;
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}*/

import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@CrossOrigin(origins = "https://spy-notes.rk0.xyz")
@RestController
public class DynamoController {
    /*private final DynamoDbTable<userDoc> userTable;
    private final DynamoDbClient dynamoClient;

    DynamoController(DynamoDbTable<userDoc> userTable, DynamoDbClient dynamoClient) {
        this.userTable = userTable;
        this.dynamoClient = dynamoClient;
    }*/


    @PostMapping("/users")
    public Map<String, Object> createUser() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();
        DynamoDbTable<userDoc> table =
                enhancedClient.table("user", TableSchema.fromBean(userDoc.class));

        UUID uuid = UUID.randomUUID(); //uuid randomizes username
        long seconds = System.currentTimeMillis(); //timestamp
        Map<String, Object> json = new HashMap<>(); //map holds timestamp and userID

        json.put("createdDate", seconds);
        json.put("userId", uuid.toString());

        /*PutItemRequest request = PutItemRequest.builder()
                        .tableName("user")
                                .item(json)
                                        .build();*/

        userDoc newUser = new userDoc();
        newUser.setUserId(uuid.toString());
        newUser.setCreatedDate(seconds);
        table.putItem(newUser);

        return json;
    }

    @GetMapping("/users")//Map<String, Object>
    public userDoc[] fetchUsers() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();

        Map<String, Object> users = new HashMap<>();
        DynamoDbTable<userDoc> table =
                enhancedClient.table("user", TableSchema.fromBean(userDoc.class));

        int usersArrayLen = 0;
        try {

            Iterator<userDoc> results = table.scan().items().iterator();

            while (results.hasNext()) {
                userDoc rec = results.next();
                usersArrayLen++;
            }

        }
        catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        userDoc usersArray[]= new userDoc[usersArrayLen];
        try {

            Iterator<userDoc> results = table.scan().items().iterator();
            int i = 0;
            while (results.hasNext()) {
                userDoc rec = results.next();
                usersArray[i] = rec;
                users.put("userId",rec.getUserId());
                users.put("createdDate", rec.getCreatedDate());
                System.out.println(rec.getUserId() + "||" + rec.getCreatedDate());
                i++;
            }

        }
        catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Done");

        //return users;
        return usersArray;
    }

    @PostMapping("/users/{userId}/notes")
    //Map<String, Object>
    public noteDoc createNote(@PathVariable("userId") String userId,
                      @RequestParam(defaultValue = "") String title,
                      @RequestParam(defaultValue = "") String content)
            throws Exception {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();
        DynamoDbTable<noteDoc> table =
                enhancedClient.table("note", TableSchema.fromBean(noteDoc.class));
        //need noteId, title, content, createdDate
        noteDoc note = new noteDoc(UUID.randomUUID().toString(),
            System.currentTimeMillis(), title, content, userId);

        //Map<String, Object> newNote = new HashMap<>();
        //newNote.put("noteId", note.getNoteId());
        //newNote.put("title", note.getTitle());
        //newNote.put("content", note.getContent());
        //newNote.put("createdDate", note.getCreatedDate());

        table.putItem(note);

        return note;
        //return newNote;
    }
    //noteDoc[]
    @GetMapping("/users/{userId}/notes")//Map<String, Object>
    public noteDoc[] fetchNotes(@PathVariable("userId") String userId) {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();

        Map<String, Object> notes = new HashMap<>();
        DynamoDbTable<noteDoc> table =
                enhancedClient.table("note", TableSchema.fromBean(noteDoc.class));

        int notesArrayLen = 0;
        try {

            Iterator<noteDoc> results = table.scan().items().iterator();

            while (results.hasNext()) {
                noteDoc rec = results.next();
                notesArrayLen++;
            }

        }
        catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        noteDoc notesArray[]= new noteDoc[notesArrayLen];
        try {
            Iterator<noteDoc> results = table.scan().items().iterator();
            int i = 0;
            while (results.hasNext()) {
                noteDoc rec = results.next();
                if (Objects.equals(rec.getUserId(), userId)) {
                    notesArray[i] = rec;
                    System.out.println(rec.getNoteId());
                    //notes.put("title",rec.getTitle());
                    //notes.put("content", rec.getContent());
                    i++;
                }

                //System.out.println(rec.getUserId() + "||" + rec.getCreatedDate());

            }

        }
        catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Done");

        //return users;
        //return notesArray;
        return notesArray;
    }

    @DeleteMapping("/users/{userId}/notes/{noteId}")//Map<String, Object>
    public noteDoc deleteNote(@PathVariable("userId") String userId, @PathVariable("noteId") String noteId) {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();
        DynamoDbTable<noteDoc> table =
                enhancedClient.table("note", TableSchema.fromBean(noteDoc.class));
        noteDoc deleteMe = new noteDoc();

        try {

            Iterator<noteDoc> results = table.scan().items().iterator();

            while (results.hasNext()) {
                noteDoc rec = results.next();
                if (Objects.equals(rec.getUserId(), userId) && Objects.equals(rec.getNoteId(), noteId)) {
                    deleteMe = rec;
                    break;
                }
            }
            table.deleteItem(deleteMe);
        }
        catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        /*HashMap<String,AttributeValue> keyToGet =
                new HashMap<String,AttributeValue>();

        keyToGet.put(noteId, AttributeValue.builder()
                .s(userId)
                .build());

        DeleteItemRequest deleteReq = DeleteItemRequest.builder()
                .tableName("note")
                .key(keyToGet)
                .build();

        try {
            client.deleteItem(deleteReq);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }*/

        return deleteMe;
    }
}
