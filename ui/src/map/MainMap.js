import { Component } from "react";
import { ReactBingmaps } from 'react-bingmaps';
import ReactLoading from 'react-loading';
import "./MainMap.css"

class MainMap extends Component {

    constructor() {
        super();
        this.state = {
            isLoaded: false,
            polygons: []
        };
    }

    
   
    componentDidMount() {
        let initialPolygons = [];
        fetch(`http://localhost:8080/service-api/weather/all/polygons`)
            .then(response => { return response.json(); })
            .then(data => {
                initialPolygons = data.map((Item) => { return Item });
                this.setState({ polygons: initialPolygons });
                this.setState({ isLoaded: true });
            });

        
    }

    render() {
        return (
            <div style={{ width: "100vw", height: "80vh" }}>
                <button className = "reload" onClick={() => window.location.reload(false)}>Click to reload</button>
                <h4 style={{color: "white"}}>.</h4>
                {this.state.isLoaded ? (
                    <>
                        <ReactBingmaps
                            bingmapKey="AoORHdJLECgUf06JJ152s4wWjjyCg5etwyoYBtJvbjUqV-OvknDXKwT9ATzETwPy"
                            center={[48.747111, 32.317217]} //center of Ukraine
                            zoom={6}
                            regularPolygons={this.state.polygons}>
                        </ReactBingmaps>
                    </>
                ) : (
                    <><ReactLoading type="balls" color="#fff" /></>
                )}
            </div>
        );
    }
}
export default MainMap;