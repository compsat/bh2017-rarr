package rarr.picnic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class contact_doctor extends AppCompatActivity {
    private Button send;
    private EditText message;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase_doctor;
    private FirebaseUser user;
    private ProgressDialog Progress;
    private ArrayList Numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_doctor);

        send = (Button) findViewById(R.id.send_message);
        message = (EditText) findViewById(R.id.message);
        Numbers = new ArrayList<>();
        init();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();

            }
        });
    }

    private void SendMessage() {
        String message1 = message.getText().toString() + " \n SENT VIA Pic-Ni-C";
        for(int i = 0; i < Numbers.size();i++){
            String mobile1 = Numbers.get(i).toString();

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("+639"+mobile1.substring(2), null,message1, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent to +639"+mobile1.substring(2), Toast.LENGTH_LONG).show();
            }

            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            Intent intent = new Intent(contact_doctor.this,Menu.class);
            startActivity(intent);
            finish();

        }

    }


    private void init(){
        Progress = new ProgressDialog(this);
        Progress.setMessage("LOADING DATA");
        Progress.show();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Nutrionist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot child : dataSnapshot.getChildren()){
                    String number = child.child("Number").getValue().toString();
                    Numbers.add(number);
                }
                Progress.dismiss();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(contact_doctor.this,Menu.class);
        startActivity(intent);
        finish();
    }
}
