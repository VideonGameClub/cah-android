package videongameclub.github.io.app.layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import videongameclub.github.io.app.R;

/**
 * Created by brobzilla on 12/9/13.
 */
public class CardLayoutAdapter extends BaseAdapter {
    /**
     * private handle to context.
     */
    private Context mContext;

    /**
     * Inner class for the ViewHolder Pattern
     */
    static class ViewHolder {
        TextView textView;
    }


    /**
     * Constructor we need a context for the getView()
     *
     * @param context context
     */
    public CardLayoutAdapter(Context context) {
        mContext = context;
    }


    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // if convertView is null, the view is newly inflated.
        // else, re-assign new values.
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cardlayout, null);

            // Set up the ViewHolder.
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.cardlayout);

            // Store the holder with the view.
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Assign values
        holder.textView.setText("HelloWorld " + position);
        return convertView;
    }
}
