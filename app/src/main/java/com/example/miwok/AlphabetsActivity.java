package com.example.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AlphabetsActivity extends AppCompatActivity {

    /** Handles playback of all the sound files */
    private MediaPlayer mMediaPlayer;

    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("ARJUN", "A", R.drawable.alphabet_arjuna, R.raw.alphabet_a));
        words.add(new Word("Balarama", "B" , R.drawable.alphabet_balarama, R.raw.alphabet_b));
        words.add(new Word("CHANAKYA", "C", R.drawable.alphabet_chanakyanjpg, R.raw.alphabet_c));
        words.add(new Word("DHRUVA", "D", R.drawable.alphabet_dhruv, R.raw.alphabet_d));
        words.add(new Word("EKALAVYA", "E", R.drawable.alphabet_ekalav, R.raw.alphabet_e));
        words.add(new Word("FOUR VEDAS ", "F", R.drawable.alphabet_fourveda, R.raw.alphabet_f));
        words.add(new Word("GAYATRI MATHA", "G", R.drawable.alphabet_gayatrimata, R.raw.alphabet_g));
        words.add(new Word("HANUMAN", "H", R.drawable.alphabet_hanuman, R.raw.alphabet_h));
        words.add(new Word("INDRA", "I", R.drawable.alphabet_indra, R.raw.alphabet_i));
        words.add(new Word("JATAYU", "J", R.drawable.alphabet_jatayu, R.raw.alphabet_j));
        words.add(new Word("KRISHNA", "K", R.drawable.alphabet_krishna, R.raw.alphabet_k));
        words.add(new Word("LAVA - KUSA", "L" , R.drawable.alphabet_lavakusajpg, R.raw.alphabet_l));
        words.add(new Word("Markandeya", "M", R.drawable.alphabet_markandey, R.raw.alphabet_m));
        words.add(new Word("Narada", "N", R.drawable.alphabet_narada, R.raw.alphabet_n));
        words.add(new Word("Omkara", "O", R.drawable.alphabet_omkara, R.raw.alphabet_o));
        words.add(new Word("Prahlada", "P", R.drawable.alphabet_prahlad, R.raw.alphabet_p));
        words.add(new Word("Queen Gandhari", "Q", R.drawable.alphabet_queengandhari, R.raw.alphabet_q));
        words.add(new Word("Rama", "R", R.drawable.alphabet_rama, R.raw.alphabet_r));
        words.add(new Word("Surya", "S", R.drawable.alphabet_surya, R.raw.alphabet_s));
        words.add(new Word("TULASI", "T", R.drawable.alphabet_tulsi, R.raw.alphabet_t));
        words.add(new Word("UDDHAVA", "U", R.drawable.alphabet_uddhava, R.raw.alphabet_u));
        words.add(new Word("VAMANAVATAR", "V", R.drawable.alphabet_vamanavatar, R.raw.alphabet_v));
        words.add(new Word("WATER GANGA", "W", R.drawable.alphabet_waterganga, R.raw.alphabet_w));
        words.add(new Word("XEERABDI DWADASI ", "X" , R.drawable.alphabet_xeerabdidwadasi, R.raw.alphabet_x));
        words.add(new Word("YASHODA", "Y", R.drawable.alphabet_yashoda, R.raw.alphabet_y));
        words.add(new Word("ZAMBAVANTHA", "Z", R.drawable.alphabet_zambavantha, R.raw.alphabet_z));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_alphabet);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        // Set a click listener to play the audio when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();

                // Get the {@link Word} object at the given position the user clicked on
                Word word = words.get(position);

                // Create and setup the {@link MediaPlayer} for the audio resource associated
                // with the current word

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){

                    mMediaPlayer = MediaPlayer.create(AlphabetsActivity.this, word.getAudioResourceId());

                    // Start the audio file
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);

                }


            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}

