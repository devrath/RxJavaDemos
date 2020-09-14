package com.demo.code.ui.operators.creationalOperators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.code.databinding.FragmentOperatorCreateBinding
import com.demo.code.models.Task
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class CreateOperatorFragment : Fragment() {

    private val TAG = CreateOperatorFragment::class.java.simpleName
    private var _binding: FragmentOperatorCreateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOperatorCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClickListeners() {
        binding.floatingActionButton.setOnClickListener {
            init()
        }
    }

    private fun init() {
        subscribeToObservable()
    }

    /**
     * Step1: Instantiate the object
     */
    private fun instantiateObject(): Task {
        return  Task("Task1", false, 3)
    }

    /**
     * Step2: Create the observable from the instantiated object
     */
    private fun createObservable(): Observable<Task> {
        return Observable.create(ObservableOnSubscribe<Task> { emitter ->
            if (!emitter.isDisposed) {
                emitter.onNext(instantiateObject())
                emitter.onComplete()
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Step3: Subscribe to the observable and the emitted object
     */
    private fun subscribeToObservable() {
        createObservable().subscribe(object : Observer<Task>{
            override fun onSubscribe(d: Disposable) {
                Timber.tag(TAG).d("Subscribe Invoked")
            }

            override fun onNext(t: Task) {
                Timber.tag(TAG).d("ScreenCurrentTask: %s", t.taskName)
            }

            override fun onError(e: Throwable) {
                Timber.tag(TAG).e("ERROR: %s",e.message)
            }

            override fun onComplete() {
                Timber.tag(TAG).d("Printing tasks are complete")
            }
        })
    }


}