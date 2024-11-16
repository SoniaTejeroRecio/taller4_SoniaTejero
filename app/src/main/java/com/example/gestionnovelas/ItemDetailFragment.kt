package com.example.gestionnovelas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ItemDetailFragment : Fragment() {

    companion object {
        private const val ARG_ITEM = "item"

        fun newInstance(item: String): ItemDetailFragment {
            val fragment = ItemDetailFragment()
            val args = Bundle()
            args.putString(ARG_ITEM, item)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_detail, container, false)
        val itemTextView: TextView = view.findViewById(R.id.itemTextView)
        val item = arguments?.getString(ARG_ITEM)
        itemTextView.text = item ?: "No item available"
        return view
    }
}