package com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.johnhigginsmavila.rcrtskotlinapp.Adapters.ReportListAdapter

import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.ReportService
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_view_reports.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ViewReportsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ViewReportsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ViewReportsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var adapter: ReportListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_view_reports, container, false)
        getReports{
            val progressBar = v.findViewById<ProgressBar>(R.id.viewReportsProgressBar)

            progressBar.visibility = View.INVISIBLE
            adapter = ReportListAdapter(context!!, ReportService.reports) { report ->

            }

            reportListView.adapter = adapter

            val layoutManager = LinearLayoutManager(context)
            reportListView.layoutManager = layoutManager

        }
        return v
    }

    fun getReports (cb: (Boolean) -> Unit) {
        ReportService.getMyReports()
            .subscribeOn(Schedulers.io())
            .subscribe{
                cb(it)
            }
            .run {

            }
    }

}
