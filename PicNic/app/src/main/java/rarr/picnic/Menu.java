package rarr.picnic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu extends AppCompatActivity {
    private DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;

    private Toolbar nToolbar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private TextView mName;



    private Uri photoUrl;
    private ImageView mPic;
    private WebView browser;
    private final Context c = this;
    private ProgressDialog Progress ;
    private NavigationView mNavigationView;

    private String   fsex;
    private EditText fname;
    private EditText birthdate;
    private EditText height;
    private EditText weight;
    private Spinner  sex;
    private EditText age;
    private String name;

    String Sname;
    String Sweight;
    String Sage;
    String Sheight_inches;
    String Sheight_feet;


    String sBirthdate;

    private ArrayList name1;
    private ArrayList age1;
    private ArrayList weight1;
    private ArrayList height1;
    private ArrayList sex1;
    private ArrayList bmi1;
    private String tag_name;
    private String tag_age;
    private String tag_weight;
    private String tag_height;
    private String tag_sex;
    private String tag_bmi;
    private Button get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        nToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(nToolbar);

        nDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, R.string.open, R.string.close);

        nDrawerLayout.addDrawerListener(nToggle);
        nToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(Menu.this,MainActivity.class));
                }
            }
        };
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mName   = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.textView);
        mPic    = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.pPic);
        getCurrentinfo();





        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_account: {
                        //Toast.makeText(Menu.this, "Account Selected", Toast.LENGTH_SHORT).show();
                        UpdateNumber();
                        return true;
                    }

                    case R.id.nav_pets: {
                        //addPet();
                        addMember();
                        return true;
                    }



                    case R.id.nav_logout: {
                        mAuth.signOut();
                        // Google sign out
                    }

                    default:
                        return true;
                }


            }


        });

        name1 = new ArrayList<>();

        age1 = new ArrayList<>();

        weight1 = new ArrayList<>();

        height1 = new ArrayList<>();

        sex1 = new ArrayList<>();

        bmi1 = new ArrayList<>();


        Progress = new ProgressDialog(this);
       Progress.setMessage("LOADING DATA");
        Progress.show();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child("Family").child(user.getUid().toString()).child("Family_members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot child : dataSnapshot.getChildren()){
                    String name = new String(child.child("Member_Name").getValue().toString());
                    String age = new String(child.child("Age").getValue().toString());
                    String BMI_result = child.child("BMI_Result").getValue().toString();
                    String Height_ft = child.child("Height_ft").getValue().toString();
                    String Height_inches = child.child("Height_inches").getValue().toString();
                    String Height = Height_ft +"'" + Height_inches +"''";
                    String Sex = child.child("Sex").getValue().toString() ;
                    String Weight = child.child("Weight").getValue().toString();
                    name1.add(name);

                    age1.add(age);

                    weight1.add(Weight);

                    height1.add(Height);

                    sex1.add(Sex);

                    bmi1.add(BMI_result);


                }
                Progress.dismiss();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



      get = (Button)findViewById(R.id.getdata);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initViews();
            }
        });




    }

    private void initViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        tag_name = new String("Name:");
        tag_age = new String("Age:");
        tag_weight = new String("Weight:");
        tag_height = new String("Height:");
        tag_sex = new String("Sex:");
        tag_bmi = new String("BMI:");
        RecyclerView.Adapter adapter = new DataAdapter(name1, age1, weight1, height1, sex1, bmi1, tag_name, tag_age, tag_weight, tag_height, tag_sex, tag_bmi);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    String bmi_get = bmi1.get(position).toString();
                    provideTips(bmi_get);

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }






    private void provideTips(String bmi_get) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.tips, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);

        final TextView info = (TextView) mView.findViewById(R.id.info_bmi);
        info.startAnimation(animationFadeIn);
        if(bmi_get.equals("OVERWEIGHT")){
            info.setText(
                    "Successful weight-loss treatments include setting goals and making lifestyle changes," +
                            "\n such as eating fewer calories and being physically active." +
                            "\nMedicines and weight-loss surgery also are options for some people if lifestyle changes aren't enough.");
        }


        else if(bmi_get.equals("UNDERWEIGHT")){
            info.setText(
                    "A healthy balanced diet is recommended for prevention of malnutrition. There are four major food groups that include:" +
                            "\n 1.\tBread, rice, potatoes, and other starchy foods. This forms the largest portion of the diet and provides calories for energy and carbohydrates that are converted to sugars which provide energy." +
                            "\n 2.\tMilk and dairy foods – Vital sources of fats and simple sugars like lactose as well as minerals like Calcium." +
                            "\n 3.\tFruit and vegetables – Vital sources of vitamins and minerals as well as fiber and roughage for better digestive health." +
                            "\n4.\tMeat, poultry, fish, eggs, beans and other non-dairy sources of protein – These form the building blocks of the body and help in numerous body and enzyme functions.\n" +

                            "\nSource: http://www.news-medical.net/health/Treatment-of-malnutrition.aspx");
        }


       else if(bmi_get.equals("OBESE")){
            info.setText(
                    "Successful weight-loss treatments include setting goals and making lifestyle changes," +
                            "\n such as eating fewer calories and being physically active." +
                            "\nMedicines and weight-loss surgery also are options for some people if lifestyle changes aren't enough.");
        }

        else{
            info.setText(
                    "Congratulations. Your family member is healthy.");
        }


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        Intent intent = new Intent(Menu.this,contact_doctor.class);
                        startActivity(intent);
                        finish();

                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }



    private void addMember() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.add_member, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        Progress    = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        alertDialogBuilderUserInput.setView(mView);


        final EditText pName = (EditText) mView.findViewById(R.id.family_name);
        final EditText pBdate = (EditText) mView.findViewById(R.id.family_bday);
        final EditText pHeight_inches = (EditText) mView.findViewById(R.id.family_height_inches);
        final EditText pHeight_feet   = (EditText) mView.findViewById(R.id.family_height_feet);
        final EditText pWeight= (EditText) mView.findViewById(R.id.family_weight);
        final EditText pAge   = (EditText) mView.findViewById(R.id.family_age);
        final Spinner spinner =  (Spinner) mView.findViewById(R.id.spinner);

        String [] sex ={
                "MALE","FEMALE"
        };

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,sex);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                fsex = parent.getItemAtPosition(pos).toString().trim();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        Sname = pName.getText().toString().trim();
                        Sweight = pWeight.getText().toString().trim();
                        Sage = pAge.getText().toString().trim();
                        Sheight_inches = pHeight_inches.getText().toString().trim();
                        Sheight_feet =   pHeight_feet.getText().toString().trim();
                        sBirthdate = pBdate.getText().toString().trim();

                        /*Calculate BMI*/
                        int totalInches = (Integer.parseInt(Sheight_feet) * 12) + Integer.parseInt(Sheight_inches);
                        double uWeight = 0.0, oWeight = 0.0, obese = 0.0;
                        double dWeight = Double.parseDouble(Sweight);
                        double dHeight = (double) totalInches;
                        double Sbmi = (dWeight/(dHeight*dHeight)) * 703;
                        String bmiRes;
                        int age = Integer.parseInt(Sage);

                        if(age <= 20) {
                            if (fsex.equals("MALE")) {
                                switch(age) {
                                    case 2: uWeight = 14.8;
                                            oWeight = 18.2;
                                            obese = 19.4;
                                            break;
                                    case 3: uWeight = 14.0;
                                            oWeight = 17.4;
                                            obese = 18.3;
                                            break;
                                    case 4: uWeight = 14.1;
                                            oWeight = 16.9;
                                            obese = 17.8;
                                            break;
                                    case 5: uWeight = 13.9;
                                            oWeight = 16.8;
                                            obese = 17.9;
                                            break;
                                    case 6: uWeight = 13.7;
                                            oWeight = 17.0;
                                            obese = 18.4;
                                            break;
                                    case 7: uWeight = 13.7;
                                            oWeight = 17.4;
                                            obese = 19.1;
                                            break;
                                    case 8: uWeight = 13.8;
                                            oWeight = 18.0;
                                            obese = 20.0;
                                            break;
                                    case 9: uWeight = 14.0;
                                            oWeight = 18.6;
                                            obese = 21.0;
                                            break;
                                    case 10: uWeight = 14.2;
                                             oWeight = 19.3;
                                             obese = 22.1;
                                             break;
                                    case 11: uWeight = 14.6;
                                             oWeight = 20.2;
                                             obese = 23.2;
                                             break;
                                    case 12: uWeight = 15.0;
                                             oWeight = 21.0;
                                             obese = 24.2;
                                             break;
                                    case 13: uWeight = 15.45;
                                             oWeight = 21.8;
                                             obese = 25.1;
                                             break;
                                    case 14: uWeight = 16.0;
                                             oWeight = 22.6;
                                             obese = 26.0;
                                             break;
                                    case 15: uWeight = 16.5;
                                             oWeight = 23.4;
                                             obese = 26.8;
                                             break;
                                    case 16: uWeight = 17.1;
                                             oWeight = 24.2;
                                             obese = 27.5;
                                             break;
                                    case 17: uWeight = 17.7;
                                             oWeight = 24.9;
                                             obese = 28.2;
                                             break;
                                    case 18: uWeight = 18.2;
                                             oWeight = 25.6;
                                             obese = 29.9;
                                             break;
                                    case 19: uWeight = 18.7;
                                             oWeight = 26.3;
                                             obese = 29.7;
                                             break;
                                    case 20: uWeight = 19.1;
                                             oWeight = 27.1;
                                             obese = 30.6;
                                             break;
                                    default: break;
                                }
                            } else { //FEMALE
                                switch(age) {
                                    case 2: uWeight = 14.6;
                                            oWeight = 18.0;
                                            obese = 19.1;
                                            break;
                                    case 3: uWeight = 14.2;
                                            oWeight = 17.2;
                                            obese = 18.3;
                                            break;
                                    case 4: uWeight = 13.7;
                                            oWeight = 16.8;
                                            obese = 18.0;
                                            break;
                                    case 5: uWeight = 13.5;
                                            oWeight = 16.8;
                                            obese = 18.2;
                                            break;
                                    case 6: uWeight = 13.4;
                                            oWeight = 17.1;
                                            obese = 18.8;
                                            break;
                                    case 7: uWeight = 13.4;
                                            oWeight = 17.6;
                                            obese = 19.8;
                                            break;
                                    case 8: uWeight = 13.6;
                                            oWeight = 18.1;
                                            obese = 20.6;
                                            break;
                                    case 9: uWeight = 13.8;
                                            oWeight = 19.1;
                                            obese = 21.7;
                                            break;
                                    case 10: uWeight = 14.0;
                                             oWeight = 19.9;
                                             obese = 22.9;
                                             break;
                                    case 11: uWeight = 14.4;
                                             oWeight = 20.8;
                                             obese = 24.1;
                                             break;
                                    case 12: uWeight = 14.8;
                                             oWeight = 21.7;
                                             obese = 25.2;
                                             break;
                                    case 13: uWeight = 15.2;
                                             oWeight = 22.6;
                                             obese = 26.8;
                                             break;
                                    case 14: uWeight = 15.8;
                                             oWeight = 23.2;
                                             obese = 27.2;
                                             break;
                                    case 15: uWeight = 16.3;
                                             oWeight = 24.0;
                                             obese = 28.1;
                                             break;
                                    case 16: uWeight = 16.8;
                                             oWeight = 24.6;
                                             obese = 28.9;
                                             break;
                                    case 17: uWeight = 17.2;
                                             oWeight = 25.2;
                                             obese = 29.6;
                                             break;
                                    case 18: uWeight = 17.6;
                                             oWeight = 25.7;
                                             obese = 30.3;
                                             break;
                                    case 19: uWeight = 17.7;
                                             oWeight = 26.1;
                                             obese = 31.0;
                                             break;
                                    case 20: uWeight = 17.8;
                                             oWeight = 26.7;
                                             obese = 31.8;
                                             break;
                                    default: break;
                                }
                            }
                        } else {
                            uWeight = 18.5;
                            oWeight = 25.0;
                            obese = 30.0;
                        }

                        if(Sbmi < uWeight) {
                            bmiRes = "UNDERWEIGHT";
                        } else if (Sbmi < oWeight) {
                            bmiRes = "HEALTHY";
                        } else if (Sbmi < obese) {
                            bmiRes = "OVERWEIGHT";
                        } else
                            bmiRes = "OBESE";





                        Progress.setMessage("Adding Family Member. Please Wait.");
                        Progress.show();

                        Sname = pName.getText().toString().trim();
                        Sweight = pWeight.getText().toString().trim();
                        Sage = pAge.getText().toString().trim();
                        Sheight_inches = pHeight_inches.getText().toString().trim();
                        Sheight_feet =   pHeight_feet.getText().toString().trim();
                        sBirthdate = pBdate.getText().toString().trim();



                        HashMap<String,String> dataMap = new HashMap<String, String>();
                        dataMap.put("Member_Name",Sname);
                        dataMap.put("Birthdate",sBirthdate);
                        dataMap.put("Height_ft",Sheight_feet);
                        dataMap.put("Height_inches",Sheight_inches);
                        dataMap.put("Weight",Sweight);
                        dataMap.put("Sex",fsex);
                        dataMap.put("Age",Sage);
                        dataMap.put("BMI_Result",bmiRes);


                        user = FirebaseAuth.getInstance().getCurrentUser();
                            mDatabase.child("Family").child(user.getUid()).child("Family_members").push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        //Toast.makeText(Menu.this,"Successful.",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(Menu.this,"Failed.",Toast.LENGTH_SHORT).show();
                                    }
                                    Progress.dismiss();
                                }
                            });
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();


    }

    private void UpdateNumber() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View rView = layoutInflaterAndroid.inflate(R.layout.account_number, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(rView);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        final EditText number = (EditText) rView.findViewById(R.id.acc_number);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                       String num = number.getText().toString().trim();
                        mDatabase.child("Accounts").child(user.getUid().toString()).child("Number").setValue(num).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Menu.this,"Successful!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(nToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                }).create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    private void getCurrentinfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                name = profile.getDisplayName();
                photoUrl = profile.getPhotoUrl();
                mName.setText(name);

                Picasso.with(getApplicationContext())
                        .load(photoUrl.toString())
                        .placeholder(R.drawable.logo)
                        .resize(150, 150)
                        .transform(new CircleTransform())
                        .centerCrop()
                        .into(mPic);


            };
        }
    }

}



