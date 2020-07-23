import {Component, Input, OnInit, QueryList, ViewChildren} from '@angular/core';
import {ApplicationItem} from '../../../core/models/ApplicationItem';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatStepper } from '@angular/material/stepper';
import {RequestInputItemComponent} from '../../../shared/request-input-item/request-input-item.component';
import {PerunAttribute} from '../../../core/models/PerunAttribute';
import {TranslateService} from '@ngx-translate/core';
import {UrnValuePair} from "../../../core/models/UrnValuePair";

@Component({
  selector: 'app-request-creation-step',
  templateUrl: './request-creation-step.component.html',
  styleUrls: ['./request-creation-step.component.scss']
})
export class RequestCreationStepComponent implements OnInit {

  constructor(
    private snackBar: MatSnackBar,
    private translation: TranslateService
  ) { }

  @ViewChildren(RequestInputItemComponent)
  items: QueryList<RequestInputItemComponent>;

  @Input()
  applicationItems: ApplicationItem[];

  @Input()
  stepper: MatStepper;

  private valueErrorText: string;

  public getPerunAttributes(): PerunAttribute[] {
    const perunAttributes: PerunAttribute[] = [];

    this.items.forEach(i => {
      const attr = i.getAttribute();
      const perunAttr = new UrnValuePair(attr.value, attr.urn);
      perunAttributes.push(perunAttr);
    });

    return perunAttributes;
  }

  private attributesHasCorrectValues(): boolean {
    const attributeItems = this.items.toArray();

    for (const i of attributeItems) {
      if (!i.hasCorrectValue()) {
        return false;
      }
    }

    return true;
  }

  private checkValues(): boolean {
    this.items.forEach(i => i.onFormSubmitted());

    if (!this.attributesHasCorrectValues()) {
      this.snackBar.open(this.valueErrorText, null, {duration: 6000});
      return false;
    }

    return true;
  }

  nextStep() {
    if (!this.checkValues()) {
      return;
    }
    this.stepper.next();
  }

  previousStep() {
    this.stepper.previous();
  }

  ngOnInit() {
    this.translation.get('REQUESTS.ERRORS.VALUES_ERROR_MESSAGE').subscribe(text => this.valueErrorText = text);
  }

}
