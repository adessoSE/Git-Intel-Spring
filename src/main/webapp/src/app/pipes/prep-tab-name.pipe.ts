import { Pipe, PipeTransform } from '@angular/core';

/**
 * @PrepTabnamePipe concats the first and last
 * segment of a route to create an appropriate Tab name.
 * 
 * E.g. 
 * "/adessoAG/members"
 * => "adessoAG/members " 
 * 
 * "/adessoAG/members/john-doe"
 * => "AdessoAG/John-Doe "
 */
@Pipe({ name: 'prepTabName' })
export class PrepTabNamePipe implements PipeTransform {

    transform(value: string) {
        let retVal = value;

        if (value) {
            let idxFirst = value.indexOf("/");
            let idxLast = value.lastIndexOf("/");

            if (value.charAt(idxFirst) === "") {
                retVal = value.slice(idxLast + 1);
            }
            else {
                let orgName = value.slice(0, idxFirst);
                retVal = orgName + "/" + value.slice(idxLast + 1);
            }

        }
        return retVal;
    }

}