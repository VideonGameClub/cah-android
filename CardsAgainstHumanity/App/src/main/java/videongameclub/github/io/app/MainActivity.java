package videongameclub.github.io.app;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.MediaRouteActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import videongameclub.github.io.app.cast.CastHandler;
import videongameclub.github.io.app.cast.CastListenerImpl;
import videongameclub.github.io.app.cast.ICastListener;
import videongameclub.github.io.app.layouts.CardLayout;
import videongameclub.github.io.app.layouts.CardLayoutAdapter;

public class MainActivity extends ActionBarActivity {

    private CastHandler mCastHandler;
    private ICastListener mCastListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCastHandler = CastHandler.getInstance(getApplicationContext());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        mCastListener = new CastListenerImpl(){

        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        mediaRouteActionProvider.setRouteSelector(mCastHandler.getMediaRouteSelector());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if (null != mCastHandler) {
            mCastHandler.addCastListener(mCastListener);
        }

        super.onResume();

    }

    @Override
    protected void onDestroy() {
        if (null != mCastHandler){
            mCastHandler.onDestroy();
        }
        super.onDestroy();
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            CardLayout cardLayout = (CardLayout) rootView.findViewById(R.id.CardLayoutGrid);
            cardLayout.setAdapter(new CardLayoutAdapter(getActivity().getApplicationContext()));
            return rootView;
        }
    }

}
