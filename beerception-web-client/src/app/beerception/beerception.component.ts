import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { UploadFileService } from '../services/upload-file.service';
import { Beerception } from '../shared/models/beerception';
import { BeerceptionService } from '../services/beerception.service';

@Component({
  selector: 'app-beerception',
  templateUrl: './beerception.component.html',
  styleUrls: ['./beerception.component.css']
})
export class BeerceptionComponent implements OnInit {

  beerFileInfo: Beerception;

  imageData: any;
  url: any;

  constructor(
    private uploadService: UploadFileService,
    private beerceptionService: BeerceptionService
  ) { }

  ngOnInit() {
    this.getBeerception();
    this.getBeerceptionInfo();
  }

  getBeerception() {
    console.log("getting beerception");

    this.uploadService.getBeerception().subscribe(
      data => {
        this.imageData = 'data:image/png;base64,' + data;
      },
      error => {
        console.log(error);
      }
    );
  }

  getBeerceptionInfo() {
    console.log("getting beerception");

    this.beerceptionService.getBeerceptionInfo().subscribe(
      res => {
        console.log(res);
        this.beerFileInfo = res;
        if (res['beerDate'])
          this.beerFileInfo.beerDate = new Date(res['beerDate']);
      },
      error => {
        console.log(error);
      }
    );
  }

  beerUp() {
    this.beerceptionService.beerUp({}).subscribe(
      res => {
        console.log(res);
        this.beerFileInfo = res;
        if (res['beerDate'])
          this.beerFileInfo.beerDate = new Date(res['beerDate']);
      },
      error => {
        console.log(error);
      }
    );
  }

  beerDown() {
    this.beerceptionService.beerDown({}).subscribe(
      res => {
        console.log(res);
        this.beerFileInfo = res;
        if (res['beerDate'])
          this.beerFileInfo.beerDate = new Date(res['beerDate']);
      },
      error => {
        console.log(error);
      }
    );
  }

}
