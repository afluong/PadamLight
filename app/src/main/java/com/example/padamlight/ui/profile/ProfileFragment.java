package com.example.padamlight.ui.profile;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.padamlight.R;
import com.github.barteksc.pdfviewer.PDFView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @Bind(R.id.cvPdfView)
    PDFView cvPdfView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, root);

        cvPdfView.fromAsset("cv_anthony_poirier.pdf").load();
        return root;
    }

}
