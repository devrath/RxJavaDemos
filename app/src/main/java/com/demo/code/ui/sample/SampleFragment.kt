package com.demo.code.ui.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.demo.code.databinding.FragmentSampleBinding
import com.demo.code.models.Task
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class SampleFragment : Fragment() {

    private val TAG = "SampleFragment"
    private var _binding: FragmentSampleBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataSource : Observable<Task>
    lateinit var disposable: CompositeDisposable
    private val taskList = ArrayList<Task>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSampleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
        dataSource = createObservable()
        dataSource.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable) {
                //disposable.add(d)
            }

            override fun onNext(t: Task) {
                Timber.i(TAG,"object: ${t.toString()}")
            }

            override fun onError(e: Throwable) { Timber.e(TAG,"Error: ${e.message}") }

            override fun onComplete() {
                Toast.makeText(activity,"Success",Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun onClickListeners() {
        binding.floatingActionButton.setOnClickListener {
            taskList.add(Task("Task-1", false))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun createObservable(): Observable<Task> {
        return Observable.fromIterable(createTasks())
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun createTasks(): ArrayList<Task> {
        taskList.add(Task("Task-1", false))
        taskList.add(Task("Task-2", false))
        taskList.add(Task("Task-3", false))
        return taskList
    }

}