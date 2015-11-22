package ayushb.com.yo_poh;

import android.content.Context;
import android.widget.ArrayAdapter;

import ayushb.com.yo_poh.YoPohClasses.Complaint;

/**
 * Created by ayushb on 22/11/15.
 */
public class ComplaintsAdapter extends ArrayAdapter<Complaint> {
    public ComplaintsAdapter(Context context, int resource) {
        super(context, resource);
    }
}
