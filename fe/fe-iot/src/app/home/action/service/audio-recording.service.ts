import { Injectable } from "@angular/core";
import RecordRTC from "recordrtc";
import moment from "moment";
import { Observable, Subject } from "rxjs";
import { HttpClient, HttpResponse } from "@angular/common/http";

interface RecordedAudioOutput {
  blob: Blob;
  title: string;
}

@Injectable({
  providedIn: 'root'
})
export class AudioRecordingService {


  constructor(private http: HttpClient){
    
  }

  private stream!: MediaStream;
  private recorder: { record: () => void; stop: (arg0: (blob: any) => void, arg1: () => void) => void } | null = null;
  private interval : any
  private startTime: moment.MomentInput;
  private _recorded = new Subject<RecordedAudioOutput>();
  private _recordingTime = new Subject<string>();
  private _recordingFailed = new Subject<string>();

getRecordedBlob(): Observable<RecordedAudioOutput> {
  return this._recorded.asObservable();
}

getRecordedTime(): Observable<string> {
  return this._recordingTime.asObservable();
}

recordingFailed(): Observable<string> {
  return this._recordingFailed.asObservable();
}

startRecording() {
  if (this.recorder) {
    // It means recording is already started or it is already recording something
    return;
  }

  this._recordingTime.next("00:00");
  navigator.mediaDevices
    .getUserMedia({ audio: true })
    .then(s => {
      this.stream = s;
      this.record();
    })
    .catch(error => {
      console.error('Error accessing microphone:', error);
      this._recordingFailed.next('Error accessing microphone');
    });
}


abortRecording() {
  this.stopMedia();
}

private record() {
  this.recorder = new RecordRTC.StereoAudioRecorder(this.stream, {
    type: "audio",
    mimeType: "audio/webm"
  });

  this.recorder.record();
  this.startTime = moment();
  this.interval = setInterval(() => {
    const currentTime = moment();
    const diffTime = moment.duration(currentTime.diff(this.startTime));
    const time =
      this.toString(diffTime.minutes()) +
      ":" +
      this.toString(diffTime.seconds());
    this._recordingTime.next(time);
  }, 1000);
}

private toString(value: string | number) {
  let val = value;
  if (!value) val = "00";
  if (value < '10') val = "0" + value;
  return val;
}

stopRecording() {
  if (this.recorder) {
    this.recorder.stop(
      (blob: any) => {
        if (this.startTime) {
          const mp3Name = encodeURIComponent(
            "audio_" + new Date().getTime() + ".mp3"
          );
          this.stopMedia();
          this._recorded.next({ blob: blob, title: mp3Name });
        }
      },
      () => {
        this.stopMedia();
        this._recordingFailed.next("Recording failed"); // Pass an error message here
      }
    );
  }
}


private stopMedia() {
  if (this.recorder) {
    this.recorder = null;
    clearInterval(this.interval);
    this.startTime = null;
    if (this.stream) {
      this.stream.getAudioTracks().forEach((track: { stop: () => any; }) => track.stop());
      this.stream = null!;
    }
  }
}
uploadFile(formData: FormData): Observable<HttpResponse<string>> {
  return this.http.post('http://localhost:8080/api/iot/v1/speech-to-text/remote', formData, { observe: 'response', responseType: 'text' });
}

}
