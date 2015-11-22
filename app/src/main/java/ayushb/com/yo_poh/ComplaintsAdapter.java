package ayushb.com.yo_poh;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ayushb.com.yo_poh.YoPohClasses.Complaint;

/**
 * Created by ayushb on 22/11/15.
 */
public class ComplaintsAdapter extends ArrayAdapter<Complaint> {
    private List<Complaint> objects;
    private FragmentActivity activity;

    public ComplaintsAdapter(Context context, List<Complaint> objects, FragmentActivity activity) {
        super(context, R.layout.complaint_list_item, objects);
        this.activity = activity;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.complaint_list_item, parent, false);
        }

        TextView productName = (TextView) convertView.findViewById(R.id.product_name);
        TextView ticketNumber = (TextView) convertView.findViewById(R.id.ticket_number);
        TextView dateCreated = (TextView) convertView.findViewById(R.id.date_created);

        productName.setText(objects.get(position).getProductName());
        ticketNumber.setText("Ticket Number: "+objects.get(position).getTicketNumber());
        Date date = new java.sql.Date(objects.get(position).getDateCreated());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateCreated.setText(sdf.format(date));


        return convertView;
    }
}
