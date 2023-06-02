import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {City, Comment, Trip} from "@app/models";
import {environment} from "@environments/environment";
import {map} from "rxjs/operators";
import {Airport} from "@app/models/Airport";

@Injectable({
    providedIn: 'root'
})
export class CityService {

    constructor(private http: HttpClient) {
    }

    public create(city: City): Observable<City> {
        return this.http
            .post<City>(`${environment.apiUrl}/v1/cities`, city)
            .pipe(map(cityView => {
                return City.of(cityView.id, cityView.name, cityView.description,
                    cityView.country, cityView.comments);
            }));
    }

    public search(searchTerms: { byName?: string, commentsLimit: number }): Observable<City[]> {

        let commentsLimit: string = searchTerms.commentsLimit ? `?cLimit=${searchTerms.commentsLimit}` : '';

        return this.http
            .post<City[]>(`${environment.apiUrl}/v1/cities/search${commentsLimit}`,
                {"byName": searchTerms.byName});
    }

    public searchAirports(byName: string): Observable<Airport[]> {
        return this.http
            .get<Airport[]>(`${environment.apiUrl}/v1/cities/airports?name=${byName}`);
    }

    public travel(from: string,to: string): Observable<Trip[]> {
        return this.http
            .get<Trip[]>(`${environment.apiUrl}/v1/cities/travel?from=${from}&to=${to}`);
    }

    // Comment management methods

    public deleteComment(cityId: number, id: number): Observable<any> {
        return this.http.delete(`${environment.apiUrl}/v1/cities/${cityId}/comments/${id}`);
    }

    public addComment(cityId: number, comment: string): Observable<Comment> {
        return this.http.post<Comment>(`${environment.apiUrl}/v1/cities/${cityId}/comments/`,
            {comment: comment});
    }

    public updateComment(cityId: number, id: number, comment: string): Observable<any> {
        return this.http.put(`${environment.apiUrl}/v1/cities/${cityId}/comments/${id}`,
            {"comment": comment});
    }

}
