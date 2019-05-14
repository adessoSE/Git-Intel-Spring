/**
 * Object containing an organisation's url and the name to be displayed in the tab.
 * Used by the dashboard component to inject the tab name and the header component to access it via the global navigation service. 
 */
export class TabNameObject {
    url: string;
    value: string;
}