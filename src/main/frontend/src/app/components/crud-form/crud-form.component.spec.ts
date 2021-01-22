import {createHostFactory, SpectatorHost} from '@ngneat/spectator';
import {CrudFormComponent} from "./crud-form.component";
import {ReactiveFormsModule} from "@angular/forms";
import {ICrudViewerConfiguration} from "../../types/CrudViewerConfiguration";

describe('CrudFormComponent', () => {
  let spectator: SpectatorHost<CrudFormComponent<TestEntity>>;
  const createHost = createHostFactory<CrudFormComponent<TestEntity>>({
    component: CrudFormComponent,
    imports: [ReactiveFormsModule]
  });

  it('should create', () => {
    spectator = createHost("<crud-form></crud-form>");
    expect(spectator.component).toBeTruthy();
  });

  it('should create form group from config with simple fields', () => {
    const config = createConfig();

    spectator = createHost("<crud-form [config]='config'></crud-form>", {
      hostProps: { config }
    });

    expect(Object.keys(spectator.component.formGroup.controls))
      .toEqual(jasmine.arrayContaining(config.fields.map(i => i.path)));
  });

  it('should create form group from config without readonly fields', () => {
    const config = createConfig();
    config.fields.push({ title: "Deparment", path: "dep", readOnly: true })

    spectator = createHost("<crud-form [config]='config'></crud-form>", {
      hostProps: { config }
    });

    const controlsSize = Object.keys(spectator.component.formGroup.controls).length;
    expect(controlsSize).toEqual(config.fields.length - 1);
  });

  it('should create form group from config without fields with complex path', () => {
    const config = createConfig();
    config.fields.push({ title: "Deparment", path: "dep.id" })

    spectator = createHost("<crud-form [config]='config'></crud-form>", {
      hostProps: { config }
    });

    const controlsSize = Object.keys(spectator.component.formGroup.controls).length;
    expect(controlsSize).toEqual(config.fields.length - 1);
  });

  it('should create form group from config without id field', () => {
    const config = createConfig();
    config.fields.push({ title: "Deparment", path: "depId", isId: true })

    spectator = createHost("<crud-form [config]='config'></crud-form>", {
      hostProps: { config }
    });

    const controlsSize = Object.keys(spectator.component.formGroup.controls).length;
    expect(controlsSize).toEqual(config.fields.length - 1);
  });

  it('should output on submit click', () => {
    const config = createConfig();
    spectator = createHost("<crud-form [config]='config'></crud-form>", {
      hostProps: { config }
    });

    spyOn(spectator.component.submitForm, 'emit');

    spectator.click("input[type='submit']");
    expect(spectator.component.submitForm.emit).toHaveBeenCalledTimes(1);
  });

  function createConfig(): ICrudViewerConfiguration {
    return {
      baseApi: "",
      fields: [
        { title: "Id", path: "id" },
        { title: "Name", path: "name" }
      ]
    }
  }
});

interface TestEntity {
  id: number;
  name: string;
}
