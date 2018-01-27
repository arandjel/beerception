import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpResponse, HttpEventType } from '@angular/common/http';
import { UploadFileService } from '../services/upload-file.service';

@Component({
  selector: 'app-create-beerception',
  templateUrl: './create-beerception.component.html',
  styleUrls: ['./create-beerception.component.css']
})
export class CreateBeerceptionComponent implements OnInit {

  selectedFiles: FileList
  currentFileUpload: File
  progress: { percentage: number } = { percentage: 0 }

  fileUploading: boolean;

  constructor(private uploadService: UploadFileService) { }

  ngOnInit() {
  }

  selectFile(event) {
    const file = event.target.files.item(0)

    if (file.type.match('image.*')) {
      this.selectedFiles = event.target.files;
    } else {
      alert('invalid format!');
    }
  }

  upload() {
    this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0);

    this.uploadService.addBeerception(this.currentFileUpload)
      .subscribe(event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.progress.percentage = Math.round(100 * event.loaded / event.total);
        } else if (event instanceof HttpResponse) {
          console.log('File is completely uploaded!');
        }
      });

    this.selectedFiles = undefined;
  }

}
