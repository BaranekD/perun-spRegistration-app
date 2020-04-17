import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ConfigService} from '../../core/services/config.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import {TranslateService} from '@ngx-translate/core';
import {ApplicationItemComponent} from '../../requests/new-request/application-item/application-item.component';
import {ApplicationItem} from '../../core/models/ApplicationItem';
import {FacilitiesService} from '../../core/services/facilities.service';
import {Facility} from '../../core/models/Facility';
import { UrnValuePair } from '../../core/models/UrnValuePair';

@Component({
  selector: 'app-facilities-edit',
  templateUrl: './facilities-edit.component.html',
  styleUrls: ['./facilities-edit.component.scss']
})
export class FacilitiesEditComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private configService: ConfigService,
    private facilityService: FacilitiesService,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    private router: Router) {
  }

  @ViewChildren(ApplicationItemComponent)
  items: QueryList<ApplicationItemComponent>;

  private sub: any;
  isFormVisible = false;
  isCardBodyVisible = false;
  loading = true;
  snackBarDurationMs = 5000;

  facility: Facility;

  showServiceMore: boolean = false;
  showOrganizationMore: boolean = false;
  showProtocolMore: boolean = false;
  showAccessControlMore: boolean = false;

  // translations
  errorText: string;
  successActionText: string;
  failedActionText: string;
  errorWronglyFilledItem: string;
  errorRequestAlreadyExists: string;

  serviceAttrs: ApplicationItem[] = [];
  organizationAttrs: ApplicationItem[] = [];
  protocolAttrs: ApplicationItem[] = [];
  accessControlAttrs: ApplicationItem[] = [];

  ngOnInit() {
    this.translate.get('FACILITIES.NEW_VALUES_ERROR_MESSAGE')
      .subscribe(value => this.errorText = value);
    this.translate.get('REQUESTS.COULD_NOT_SUBMIT_EMPTY')
      .subscribe(value => this.failedActionText = value);
    this.translate.get('REQUESTS.SUCCESSFULLY_SUBMITTED')
      .subscribe(value => this.successActionText = value);
    this.translate.get('FACILITIES.WRONGLY_FILLED_ITEM')
      .subscribe(value => this.errorWronglyFilledItem = value);
    this.translate.get('FACILITIES.ERROR_REQUEST_ALREADY_EXISTS')
      .subscribe(value => this.errorRequestAlreadyExists = value);
    this.getAttributes();
  }

  revealForm() {
    this.loading = false;
    this.isCardBodyVisible = true;
    this.isFormVisible = true;
  }

  submitRequest() {
    this.loading = true;
    if (this.facility.activeRequestId != null) {
      this.snackBar.open(this.errorRequestAlreadyExists, null, {duration: 6000});
      return;
    }

    const perunAttributes: UrnValuePair[] = [];

    // set to false when one attribute has wrong value
    let allGood = true;
    this.items.forEach(i => {
      const attr = i.getAttribute();
      const perunAttr = new UrnValuePair(attr.value, attr.urn);
      if (!i.hasCorrectValue()) {
        this.snackBar.open(this.errorWronglyFilledItem, null, {duration: 6000});
        allGood = false;
        this.loading = false;
        return;
      }
      perunAttributes.push(perunAttr);
    });

    if (!allGood) {
      return;
    }

    this.facilityService.changeFacility(this.facility.id, perunAttributes).subscribe(reqId => {
      if (reqId === null) {
        this.loading = false;
        this.snackBar.open(this.failedActionText, null, {duration: this.snackBarDurationMs});
        return;
      } else {
        this.loading = false;
        this.snackBar.open(this.successActionText, null, {duration: this.snackBarDurationMs});
        this.router.navigate(['/auth/requests/detail/' + reqId]);
      }
    });
  }

  private getAttributes(): void {
    this.sub = this.route.params.subscribe(params => {
      this.facilityService.getFacilityWithInputs(params['id']).subscribe(facility => {
        this.facility = new Facility(facility);
        this.pushInputs();
        this.filterAndSortArrays();
        this.revealForm();
        this.loading = false;
      }, error => {
        this.loading = false;
        console.log(error);
      });
    });
  }

  private pushInputs() {
    this.clearArrays();
    this.facility.serviceAttrs().forEach((attr, _) => {
      this.serviceAttrs.push(attr.input);
    });

    this.facility.organizationAttrs().forEach((attr, _) => {
      this.organizationAttrs.push(attr.input);
    });

    this.facility.protocolAttrs().forEach((attr, _) => {
      this.protocolAttrs.push(attr.input);
    });

    this.facility.accessControlAttrs().forEach((attr, _) => {
      this.accessControlAttrs.push(attr.input);
    });
  }

  private filterAndSortArrays() {
    this.serviceAttrs = FacilitiesEditComponent.filterAndSort(this.serviceAttrs);
    this.organizationAttrs = FacilitiesEditComponent.filterAndSort(this.organizationAttrs);
    this.protocolAttrs = FacilitiesEditComponent.filterAndSort(this.protocolAttrs);
    this.accessControlAttrs = FacilitiesEditComponent.filterAndSort(this.accessControlAttrs);
  }

  clearArrays(): void {
    this.serviceAttrs = [];
    this.organizationAttrs = [];
    this.protocolAttrs = [];
    this.accessControlAttrs = [];
  }

  private changeServiceShowMore() {
    this.showServiceMore = !this.showServiceMore;
    this.ngOnInit();
  }

  private changeOrganizationShowMore() {
    this.showOrganizationMore = !this.showOrganizationMore;
    this.ngOnInit();
  }

  private changeProtocolShowMore() {
    this.showProtocolMore = !this.showProtocolMore;
    this.ngOnInit();
  }

  private changeAccessShowMore() {
    this.showAccessControlMore = !this.showAccessControlMore;
    this.ngOnInit();
  }

  private static filterAndSort(items: ApplicationItem[]): ApplicationItem[] {
    items = this.filterItems(items);
    items = this.sortItems(items);
    return items;
  }

  private static filterItems(items: ApplicationItem[]): ApplicationItem[] {
    items.filter((item) => { return item.displayed });
    return items
  }

  private static sortItems(items: ApplicationItem[]): ApplicationItem[] {
    items.sort((a, b) => {
      return a.displayPosition - b.displayPosition;
    });

    return items;
  }

}

