package mufanc.tools.myandroidtools.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mufanc.tools.myandroidtools.databinding.FragmentProcessBinding

class ProcessFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentProcessBinding.inflate(inflater).apply {

            return root
        }
    }
}