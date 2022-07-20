package xyz.rafizuddin.fossdic;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class customAAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] title;
    private final String[] subTitle;
    public customAAdapter(Activity context, String[] title, String[] subTitle){
        super(context, R.layout.list,title);
        this.context=context;
        this.title=title;
        this.subTitle=subTitle;

    }
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list,null,true);
        TextView titleText=rowView.findViewById(R.id.Ltitle);
        TextView subText=rowView.findViewById(R.id.subTitle);
        titleText.setText(title[position]);
        if(subTitle[position]!=null){
        subText.setText(Html.fromHtml(subTitle[position]));}
        else{
            subText.setText("-");
        }
        //subText.setText(subTitle[position]);
        subText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(subTitle[position]!=null){
                    setClipboard(getContext(),subTitle[position]);}
                else{
                    subText.setText("-");
                }


                return true;
            }
        });
        return rowView;
    }
public void setClipboard(Context cntx, String text){
        ClipboardManager clp=(ClipboardManager) cntx.getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip=ClipData.newHtmlText("Swoccho dic",Html.fromHtml(text).toString(),text);
    clp.setPrimaryClip(clip);
    Toast.makeText(cntx, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
}
}
