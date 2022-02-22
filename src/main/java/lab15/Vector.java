package lab15;

public class Vector {
    double x;
    double y;

    Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    Vector add(Vector v) {
        return new Vector(this.x + v.x, this.y + v.y);
    }

    Vector subtract(Vector v) {
        return new Vector(this.x - v.x, this.y - v.y);
    }

    double dot(Vector v) {
        return this.x * v.x + this.y * v.y;
    }

    Vector multiply(Vector v) {
        return new Vector(this.x * v.x, this.y * v.y);
    }

    Vector multiply(double d) {
        return new Vector(this.x * d, this.y * d);
    }

    Vector divide(Vector v) {
        return new Vector(this.x / v.x, this.y / v.y);
    }

    Vector divide(double d) {
        return new Vector(this.x / d, this.y / d);
    }

    double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }
}
