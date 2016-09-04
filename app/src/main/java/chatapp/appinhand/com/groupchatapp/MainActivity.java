package chatapp.appinhand.com.groupchatapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class MainActivity extends ActionBarActivity {

    ListView listview;
    ArrayAdapter<String> arrayadapter;
    ArrayList<String> arraylist;
Button addroom;
    EditText roomname;
String username_dialog;
    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlistview);
       arraylist =new ArrayList<>();
        root= FirebaseDatabase.getInstance().getReference().getRoot();
        addroom= (Button) findViewById(R.id.addroom);
        roomname= (EditText) findViewById(R.id.romename);
        listview= (ListView) findViewById(R.id.chat_listview);
        arrayadapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraylist);
listview.setAdapter(arrayadapter);
    username();

addroom.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put(roomname.getText().toString(),"");
        root.updateChildren(map);
    }
});

       // singl eview chat app

//        root.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                append_chat_conversion(dataSnapshot);
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });





        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<String>();
                Iterator i =dataSnapshot.getChildren().iterator();

                while ((i.hasNext()))
                {
                set.add(((DataSnapshot) i.next()).getKey());
                }
                arraylist.clear();
                arraylist.addAll(set);
                arrayadapter.notifyDataSetChanged();
                //for single view chat app
                //root.push().setValue(modellclass name)
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        Intent intent=new Intent(getApplicationContext(),chatroom.class);
        intent.putExtra("roomname",  listview.getItemAtPosition(i).toString());
        intent.putExtra("username",username_dialog);
        startActivity(intent);
    }
});


    }


////for singl eviwew chat app
//    private void append_chat_conversion(DataSnapshot dataSnapshot)
//    {
//
////        Set<String> set=new HashSet<String>();
//        Iterator i =dataSnapshot.getChildren().iterator();
//
//        while ((i.hasNext()))
//        {
//            chat_msg=(String)(((DataSnapshot) i.next()).getValue());
//            chat_username=(String)(((DataSnapshot) i.next()).getValue());
//
//        }
//        allMsgs.append(chat_username+"\n"+chat_msg+"\n");
//
//
//    }


    public  void  username()
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Username");

        final EditText edit=new EditText(this);
        dialog.setView(edit);

dialog.setPositiveButton("ok",new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
           username_dialog= edit.getText().toString();
    }
});
        dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                username();
            }
        });
dialog.show();

    }
}
