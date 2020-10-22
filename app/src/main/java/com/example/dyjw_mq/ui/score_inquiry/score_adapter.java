package com.example.dyjw_mq.ui.score_inquiry;



        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.res.Resources;
        import android.graphics.drawable.Drawable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.LinearLayout;
        import android.widget.TextView;


        import com.example.dyjw_mq.R;

        import java.util.ArrayList;


public class score_adapter extends BaseAdapter {
    public int count;
    protected Context context;
    protected LayoutInflater inflater;
    protected int resource;
    protected ArrayList<score_datainfo> data;
    public score_adapter(Context context,  int count,ArrayList<score_datainfo> data){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.resource = resource;
        this.count = count;
        if(data==null){
            this.data=new ArrayList<>();
        }else{
            this.data = data;
        }
    }


    @Override
    public int getCount() {
        return data.size();

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.showscore, null);


        TextView course_name_tv = view.findViewById(R.id.course_name);
        TextView course_type_tv = view.findViewById(R.id.course_type);
        TextView credit_tv = view.findViewById(R.id.credit);
        TextView gpa_tv = view.findViewById(R.id.gpa);
        TextView score_tv = view.findViewById(R.id.score);
        LinearLayout ispass= view.findViewById(R.id.ispass);


if(data.get(position).getCredit().equals(""))
{
    course_name_tv.setText(data.get(position).getCourse_name());
    course_type_tv.setText(data.get(position).getCourse_type());
    credit_tv.setText(data.get(position).getCredit());
    gpa_tv.setText(data.get(position).getGpa());
    score_tv.setText(data.get(position).getScore());

}
else {
    course_name_tv.setText(data.get(position).getCourse_name());
    course_type_tv.setText(data.get(position).getCourse_type());
    credit_tv.setText("学分:" + data.get(position).getCredit());
    gpa_tv.setText("GPA:" + data.get(position).getGpa());
    score_tv.setText(data.get(position).getScore());
    if(Integer.parseInt(data.get(position).getScore())<60){
//        不及格
        Resources resources = context.getResources();
        @SuppressLint("UseCompatLoadingForDrawables") Drawable btnDrawable = resources.getDrawable(R.drawable.b2);
        ispass.setBackground(btnDrawable);
    }
    else if(Integer.parseInt(data.get(position).getScore())<70){
//        合格
        Resources resources = context.getResources();
        @SuppressLint("UseCompatLoadingForDrawables") Drawable btnDrawable = resources.getDrawable(R.drawable.b4);
        ispass.setBackground(btnDrawable);
    }
    else if(Integer.parseInt(data.get(position).getScore())<90) {
//        合格
        Resources resources = context.getResources();
        @SuppressLint("UseCompatLoadingForDrawables") Drawable btnDrawable = resources.getDrawable(R.drawable.b3);
        ispass.setBackground(btnDrawable);
    }
    else
    {
//        合格
        Resources resources = context.getResources();
        @SuppressLint("UseCompatLoadingForDrawables") Drawable btnDrawable = resources.getDrawable(R.drawable.b1);
        ispass.setBackground(btnDrawable);
    }
}

        return view;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }
}
