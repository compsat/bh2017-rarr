package rarr.picnic;

/**
 * Created by REUBEN on 19/02/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<String> name1;
    private ArrayList<String> age1;
    private ArrayList<String> weight1;
    private ArrayList<String> height1;
    private ArrayList<String> sex1;
    private ArrayList<String> bmi1;
    private String tag_name;
    private String tag_age;
    private String tag_weight;
    private String tag_height;
    private String tag_sex;
    private String tag_bmi;

    public DataAdapter(ArrayList<String> name, ArrayList<String> age, ArrayList<String> weight, ArrayList<String> height, ArrayList<String> sex, ArrayList<String> bmi, String tag_name, String tag_age, String tag_weight, String tag_height, String tag_sex, String tag_bmi) {
        this.name1 = name;
        this.age1 = age;
        this.weight1 = weight;
        this.height1 = height;
        this.sex1 = sex;
        this.bmi1 = bmi;
        this.tag_name = tag_name;
        this.tag_age = tag_age;
        this.tag_weight = tag_weight;
        this.tag_height = tag_height;
        this.tag_sex = tag_sex;
        this.tag_bmi = tag_bmi;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {
        viewHolder.name1.setText(name1.get(i));
        viewHolder.age1.setText(age1.get(i));
        viewHolder.weight1.setText(weight1.get(i));
        viewHolder.height1.setText(height1.get(i));
        viewHolder.sex1.setText(sex1.get(i));
        viewHolder.bmi1.setText(bmi1.get(i));
        viewHolder.tag_name.setText(tag_name);
        viewHolder.tag_age.setText(tag_age);
        viewHolder.tag_weight.setText(tag_weight);
        viewHolder.tag_height.setText(tag_height);
        viewHolder.tag_sex.setText(tag_sex);
        viewHolder.tag_bmi.setText(tag_bmi);
    }

    @Override
    public int getItemCount() {
        return name1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name1;
        private TextView age1;
        private TextView weight1;
        private TextView height1;
        private TextView sex1;
        private TextView bmi1;
        private TextView tag_name;
        private TextView tag_age;
        private TextView tag_weight;
        private TextView tag_height;
        private TextView tag_sex;
        private TextView tag_bmi;
        public ViewHolder(View view) {
            super(view);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                }
            });
            name1 = (TextView)view.findViewById(R.id.name1);
            age1 = (TextView)view.findViewById(R.id.age1);
            weight1 = (TextView)view.findViewById(R.id.weight1);
            height1 = (TextView)view.findViewById(R.id.height1);
            sex1 = (TextView)view.findViewById(R.id.sex1);
            bmi1 = (TextView)view.findViewById(R.id.bmi1);
            tag_name = (TextView)view.findViewById(R.id.tag_name);
            tag_age = (TextView)view.findViewById(R.id.tag_age);
            tag_weight = (TextView)view.findViewById(R.id.tag_weight);
            tag_height = (TextView)view.findViewById(R.id.tag_height);
            tag_sex = (TextView)view.findViewById(R.id.tag_sex);
            tag_bmi = (TextView)view.findViewById(R.id.tag_bmi);
        }
    }

}