package com.example.videoeditor.UI.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.Adapters.ColorPickerAdapter;
import com.example.videoeditor.Adapters.FontsAdapter;
import com.example.videoeditor.R;

import java.util.ArrayList;

/**
 * Created by Burhanuddin Rashid on 1/16/2018.
 */

public class TextEditorDialogFragment extends DialogFragment {
    Typeface selected;
    ArrayList<Typeface>array = new ArrayList<Typeface>();
    public static final String TAG = TextEditorDialogFragment.class.getSimpleName();
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    private EditText mAddTextEditText;
    private TextView mAddTextDoneTextView;
    private InputMethodManager mInputMethodManager;
    private int mColorCode;
    private TextEditor mTextEditor;
    private ImageView showFonts;
    public interface TextEditor {
        void onDone(String inputText, int colorCode,Typeface tf);
    }


    //Show dialog with provide text and text color
    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity,
                                                @NonNull String inputText,
                                                @ColorInt int colorCode) {
        Bundle args = new Bundle();
        args.putString(EXTRA_INPUT_TEXT, inputText);
        args.putInt(EXTRA_COLOR_CODE, colorCode);
        TextEditorDialogFragment fragment = new TextEditorDialogFragment();
        fragment.setArguments(args);
        fragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return fragment;
    }

    //Show dialog with default text input as empty and text color white
    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity) {
        return show(appCompatActivity,
                "", ContextCompat.getColor(appCompatActivity, R.color.white));
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //Make dialog full screen with transparent background
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_text_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView addTextColorPickerRecyclerView = view.findViewById(R.id.add_text_color_picker_recycler_view);
        RecyclerView addTextFontsPickerRecyclerView = view.findViewById(R.id.FontsRV);
        mAddTextEditText = view.findViewById(R.id.add_text_edit_text);
        mAddTextEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                addTextFontsPickerRecyclerView.setVisibility(View.GONE);
            }
        });
        showFonts = view.findViewById(R.id.showFonts);
        showFonts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view1 = getActivity().getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view1 == null) {
                    view1 = new View(getActivity());
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                addTextFontsPickerRecyclerView.setVisibility(View.VISIBLE);
            }
        });
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mAddTextDoneTextView = view.findViewById(R.id.add_text_done_tv);

        mAddTextEditText.setSelection(0);
        //Setup the color picker for text color

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextFontsPickerRecyclerView.setLayoutManager(layoutManager2);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getActivity());
        //This listener will change the text color when clicked on any color from picker
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                mColorCode = colorCode;
                mAddTextEditText.setTextColor(colorCode);
            }
        });

        initArrayFonts();

        FontsAdapter fontsAdapter = new FontsAdapter(getContext(),array);
        fontsAdapter.setOnFontsPickerClickListener(new FontsAdapter.OnFontsPickerClickListener() {
            @Override
            public void OnFontsPickerClickListener(Typeface typeface) {
                mAddTextEditText.setTypeface(typeface);
                selected=typeface;
            }
        });
        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        addTextFontsPickerRecyclerView.setAdapter(fontsAdapter);
        mAddTextEditText.setText(getArguments().getString(EXTRA_INPUT_TEXT));
        mColorCode = getArguments().getInt(EXTRA_COLOR_CODE);
        Log.e("hadi hya",getArguments().getString(EXTRA_INPUT_TEXT));
        mAddTextEditText.setTextColor(mColorCode);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        //Make a callback on activity when user is done with text editing
        mAddTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dismiss();
                String inputText = mAddTextEditText.getText().toString();
                if (!TextUtils.isEmpty(inputText) && mTextEditor != null) {
                    Log.e( "onClick: ", inputText);
                    mTextEditor.onDone(inputText, mColorCode,selected);
                }
            }
        });

    }

    private void initArrayFonts() {
        array.add(Typeface.createFromAsset(getActivity().getAssets(),"fonts/f01.ttf"));
        array.add(Typeface.createFromAsset(getActivity().getAssets(),"fonts/f02.ttf"));
        array.add(Typeface.createFromAsset(getActivity().getAssets(),"fonts/f04.ttf"));
        array.add(Typeface.createFromAsset(getActivity().getAssets(),"fonts/f05.ttf"));
        array.add(Typeface.createFromAsset(getActivity().getAssets(),"fonts/f06.ttf"));
        array.add(Typeface.createFromAsset(getActivity().getAssets(),"fonts/f07.ttf"));
        array.add(Typeface.createFromAsset(getActivity().getAssets(),"fonts/f08.ttf"));
        array.add(Typeface.createFromAsset(getActivity().getAssets(),"fonts/f09.ttf"));
        array.add(Typeface.createFromAsset(getActivity().getAssets(),"fonts/f10.ttf"));
        array.add(Typeface.createFromAsset(getActivity().getAssets(),"fonts/f11.ttf"));
        array.add(Typeface.createFromAsset(getActivity().getAssets(),"fonts/f12.ttf"));
        array.add(Typeface.createFromAsset(getActivity().getAssets(),"fonts/f13.ttf"));
    }


    //Callback to listener if user is done with text editing
    public void setOnTextEditorListener(TextEditor textEditor) {
        mTextEditor = textEditor;
    }
}
