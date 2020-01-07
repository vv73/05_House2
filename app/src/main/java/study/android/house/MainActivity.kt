package study.android.house


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbox.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        settings.setOnClickListener{
                if (toolbox.getVisibility() == View.GONE)
                    toolbox.setVisibility(View.VISIBLE);
                else
                    toolbox.setVisibility(View.GONE);
            }
        // Окно на крыше
        roofWindowCheckbox.setOnCheckedChangeListener{buttonView, isChecked ->
             roofWindow.visibility = if (isChecked) View.VISIBLE else View.GONE;
        }

        stagePicker.minValue = 1
        stagePicker.maxValue = 3
        stagePicker.setOnValueChangedListener { picker, oldVal, newVal -> setStageCount(newVal) }

        //Положение дерева
        treeOffsetSlider.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                //узнаем размеры окна и максимальное смещение
                val availableWidth = (tree.parent as View).width;
                val availableOffset = Math.max(0, availableWidth - tree.width)
                // Меняем отступ слева у дерева
                (tree.layoutParams as ViewGroup.MarginLayoutParams).leftMargin = availableOffset * progress / 100
                // Применяем изменения
                tree.requestLayout()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Размер дерева
        treeSize.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                try {
                    val newSize = Integer.valueOf(s.toString())
                    val lp = tree.getLayoutParams() as MarginLayoutParams
                    lp.width = newSize
                    lp.height = (1.21 * lp.width).toInt()
                    tree.requestLayout()
                } catch(_: NumberFormatException){

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        // Ориентация двери
        radioGroupDoorОrientation.setOnCheckedChangeListener{group, checkedId ->
            if (checkedId == R.id.radioDoorLeft) {
                door.setImageResource(R.drawable.door_reverse)
            } else {
                door.setImageResource(R.drawable.door)
            }
        }

        // Выбор времени суток
        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            changeDaytime(
                hourOfDay,
                minute
            )
        }
    }
    //Установка этажности
    private fun setStageCount(count: Int) {
        if (count == 3) {
            window2.visibility = View.VISIBLE
            window3.visibility = View.VISIBLE
        }
        if (count == 2) {
            window2.visibility = View.VISIBLE
            window3.visibility = View.GONE
        }
        if (count == 1) {
            window2.visibility = View.GONE
            window3.visibility = View.GONE
        }
    }

    // ---------------------------------------------
    // Функция для организации смены дня и ночи
    // ---------------------------------------------
    private fun changeDaytime(hour: Int, minute: Int) {

        val dayTimeHour = hour + minute / 60f
        val dark = if (dayTimeHour < 3 || dayTimeHour > 21) {
            1f
        } else {
            Math.abs(dayTimeHour - 12) / 9
        }
        val light = 1 - dark
        val red = (0xff * light + 0x00 * dark).toInt()
        val green = (0xff * light + 0x18 * dark).toInt()
        val blue = (0xff * light + 0x3e * dark).toInt()
        val isDay = dayTimeHour > 6 && dayTimeHour <= 18
        val skyColor: Int = Color.rgb(red, green, blue)
        canvas.setBackgroundDrawable(ColorDrawable(skyColor))
        var roofWindowImage = R.drawable.roof_window
        var windowImage = R.drawable.window
        if (!isDay) {
            roofWindowImage = R.drawable.roof_window_night
            windowImage = R.drawable.window_night
        }

        roofWindow.setImageResource(roofWindowImage)
        leftWindow.setImageResource(windowImage)
        rightWindow.setImageResource(windowImage)
        window2.setImageResource(windowImage)
        leftWindow2.setImageResource(windowImage)
        rightWindow2.setImageResource(windowImage)
        window3.setImageResource(windowImage)
        leftWindow3.setImageResource(windowImage)
        rightWindow3.setImageResource(windowImage)
    }
}
