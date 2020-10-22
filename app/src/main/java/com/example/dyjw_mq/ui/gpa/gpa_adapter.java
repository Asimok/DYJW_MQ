package com.example.dyjw_mq.ui.gpa;




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


public class gpa_adapter extends BaseAdapter {
    public int count;
    protected Context context;
    protected LayoutInflater inflater;
    protected int resource;
    protected ArrayList<gpa_datainfo> data;
    public gpa_adapter(Context context,  int count,ArrayList<gpa_datainfo> data){
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

        View view = View.inflate(context, R.layout.showgpa, null);


        TextView mc_tv = view.findViewById(R.id.mc);
        TextView pjxfjd_tv = view.findViewById(R.id.pjxfjd);




            mc_tv.setText(data.get(position).getMc());
            pjxfjd_tv.setText(data.get(position).getPjxfjd());

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
